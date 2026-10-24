package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.apirestapp.dto.ListadoAlumnoDTO;
import com.cunoc.colegio.apirestapp.dto.PaginaAlumnosDTO;
import com.cunoc.colegio.database.DBConnectionSingleton;
import com.cunoc.colegio.database.entidades.Estado;
import com.cunoc.colegio.database.entidades.Estudiante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    public Estudiante obtenerEstudiantePorIdUsuario(int idUsuario) {
        
        Estudiante estudiante = null;
        
        String query = "SELECT * FROM estudiante WHERE id_usuario = ?";

        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idUsuario);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    estudiante = new Estudiante();
                    estudiante.setIdEstudiante(rs.getInt("id_estudiante"));
                    estudiante.setIdUsuario(rs.getInt("id_usuario"));
                    estudiante.setNombres(rs.getString("nombres"));
                    estudiante.setApellidos(rs.getString("apellidos"));
                    estudiante.setInfoMedica(rs.getString("info_medica"));
                    estudiante.setDatosEncargados(rs.getString("datos_encargados"));
                    String estadoDesdeDB = rs.getString("estado");
                    
                    if(estadoDesdeDB != null){
                        String estadoNormalizado = estadoDesdeDB.toUpperCase();
                        estudiante.setEstado(Estado.valueOf(estadoNormalizado));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el estudiante: " + e.getMessage());
        }
        
        return estudiante;
    }
    
    
    public List<Estudiante> obtenerAlumnosSinUsuario() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT id_estudiante, nombres, apellidos FROM estudiante WHERE id_usuario IS NULL AND estado = 'Activo'";
        
        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Estudiante est = new Estudiante();
                est.setIdEstudiante(rs.getInt("id_estudiante"));
                est.setNombres(rs.getString("nombres"));
                est.setApellidos(rs.getString("apellidos"));
                lista.add(est);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar huérfanos: " + e.getMessage());
        }
        return lista;
    }
    
    
    public PaginaAlumnosDTO obtenerAlumnosConUsuarioPaginado(int pagina, int limite) {
        PaginaAlumnosDTO respuesta = new PaginaAlumnosDTO();
        List<ListadoAlumnoDTO> lista = new ArrayList<>();
        
        String sqlCount = "SELECT COUNT(*) FROM estudiante WHERE id_usuario IS NOT NULL";

        String sqlData = 
            "SELECT u.id_usuario, u.correo, u.activo AS estado_usuario, " +
            "e.id_estudiante, e.nombres, e.apellidos, " +
            "g.nombre AS nombre_grado, n.nombre AS nombre_nivel, c.nombre AS nombre_carrera " +
            "FROM estudiante e " +
            "INNER JOIN usuario u ON e.id_usuario = u.id_usuario " +
            
            "LEFT JOIN (" +
            "    SELECT i1.* FROM inscripcion i1 " +
            "    INNER JOIN (" +
            "        SELECT id_estudiante, MAX(id_inscripcion) AS ultima_inscripcion " +
            "        FROM inscripcion GROUP BY id_estudiante" +
            "    ) i2 ON i1.id_inscripcion = i2.ultima_inscripcion" +
            ") i ON e.id_estudiante = i.id_estudiante " +
            
            "LEFT JOIN grado g ON i.id_grado = g.id_grado " +
            "LEFT JOIN nivel n ON g.id_nivel = n.id_nivel " +
            "LEFT JOIN carrera c ON g.id_carrera = c.id_carrera " + 
            "WHERE e.id_usuario IS NOT NULL " +
            "ORDER BY e.id_estudiante DESC LIMIT ? OFFSET ?";

        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement psCount = conexion.prepareStatement(sqlCount);
             ResultSet rsCount = psCount.executeQuery()) {
             
            int totalRegistros = 0;
            if (rsCount.next()) totalRegistros = rsCount.getInt(1);
            
            int totalPaginas = (int) Math.ceil((double) totalRegistros / limite);
            int offset = (pagina - 1) * limite;
            
            try (PreparedStatement psData = conexion.prepareStatement(sqlData)) {
                psData.setInt(1, limite); 
                psData.setInt(2, offset);
                
                try (ResultSet rsData = psData.executeQuery()) {
                    while (rsData.next()) {
                        ListadoAlumnoDTO dto = new ListadoAlumnoDTO();
                        dto.setIdUsuario(rsData.getInt("id_usuario"));
                        dto.setIdEstudiante(rsData.getInt("id_estudiante"));
                        dto.setNombres(rsData.getString("nombres"));
                        dto.setApellidos(rsData.getString("apellidos"));
                        dto.setCorreo(rsData.getString("correo"));
                        dto.setEstado(rsData.getBoolean("estado_usuario"));                        
                        dto.setGrado(rsData.getString("nombre_grado") != null ? rsData.getString("nombre_grado") : "Sin asignar");
                        dto.setNivel(rsData.getString("nombre_nivel") != null ? rsData.getString("nombre_nivel") : "Sin asignar");
                        dto.setCarrera(rsData.getString("nombre_carrera") != null ? rsData.getString("nombre_carrera") : "N/A");
                        
                        lista.add(dto);
                    }
                }
            }
            respuesta.setAlumnos(lista);
            respuesta.setTotalRegistros(totalRegistros);
            respuesta.setTotalPaginas(totalPaginas);

        } catch (SQLException e) {
            System.err.println("Error al listar alumnos: " + e.getMessage());
        }
        return respuesta;
    }
    
    
    public boolean enlazarUsuarioAEstudiante(int idEstudiante, String correo, String password, int idRolAlumno) {
        Connection conexion = null;
        PreparedStatement psUsuario = null;
        PreparedStatement psEstudiante = null;
        ResultSet rsKeys = null;

        try {
            conexion = DBConnectionSingleton.getInstance().getConnection();
            conexion.setAutoCommit(false); 

            String sqlUsuario = "INSERT INTO usuario (correo, contrasena, id_rol, activo) VALUES (?, ?, ?, true)";
            psUsuario = conexion.prepareStatement(sqlUsuario, java.sql.Statement.RETURN_GENERATED_KEYS);
            psUsuario.setString(1, correo);
            psUsuario.setString(2, password);
            psUsuario.setInt(3, idRolAlumno);
            psUsuario.executeUpdate();

            rsKeys = psUsuario.getGeneratedKeys();
            int idUsuarioGenerado = 0;
            if (rsKeys.next()) {
                idUsuarioGenerado = rsKeys.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el ID del usuario.");
            }

            String sqlEstudiante = "UPDATE estudiante SET id_usuario = ? WHERE id_estudiante = ?";
            psEstudiante = conexion.prepareStatement(sqlEstudiante);
            psEstudiante.setInt(1, idUsuarioGenerado);
            psEstudiante.setInt(2, idEstudiante);
            
            int filasActualizadas = psEstudiante.executeUpdate();
            
            if (filasActualizadas == 0) {
                throw new SQLException("No se encontró el estudiante para actualizar.");
            }

            conexion.commit();
            return true;

        } catch (Exception e) {
            if (conexion != null) {
                try {
                    conexion.rollback();
                    System.out.println("Se ejecutó Rollback: " + e.getMessage());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try { 
                if (rsKeys != null) rsKeys.close(); 
            } catch (SQLException e) { }
            try {
                if (psEstudiante != null) psEstudiante.close();
            } catch (SQLException e) { }
            try { 
                if (psUsuario != null) psUsuario.close();
            } catch (SQLException e) { }
            try { 
                if (conexion != null) {
                    conexion.setAutoCommit(true); conexion.close();
                } 
            } catch (SQLException e) { }
        }
    }
}