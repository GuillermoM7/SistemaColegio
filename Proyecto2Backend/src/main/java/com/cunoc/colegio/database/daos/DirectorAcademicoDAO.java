package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.apirestapp.dto.AsignacionDTO;
import com.cunoc.colegio.apirestapp.dto.CrearSeccionDTO;
import com.cunoc.colegio.apirestapp.dto.GradoCurriculoDTO;
import com.cunoc.colegio.apirestapp.dto.SeccionDTO;
import com.cunoc.colegio.database.DBConnectionSingleton;
import com.cunoc.colegio.database.entidades.Curso;
import com.cunoc.colegio.database.entidades.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DirectorAcademicoDAO {

    
    public List<GradoCurriculoDTO> listarGradosConEstadoCurriculo(int idNivel, Integer idCarrera) {
        List<GradoCurriculoDTO> lista = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT g.id_grado, g.nombre AS nombre_grado, c.nombre AS nombre_carrera, ");
        sql.append("EXISTS (");
        sql.append(" SELECT 1 FROM curriculo cur ");
        sql.append(" JOIN anio_lectivo al ON cur.id_anio_lectivo = al.id_anio_lectivo ");
        sql.append(" WHERE cur.id_grado = g.id_grado AND al.activo = 1");
        sql.append(") AS tiene_curriculo ");
        sql.append("FROM grado g ");
        sql.append("LEFT JOIN carrera c ON g.id_carrera = c.id_carrera ");
        sql.append("WHERE g.id_nivel = ? ");
        
        if (idCarrera != null) {
            sql.append("AND g.id_carrera = ? ");
        }
        sql.append("ORDER BY g.id_grado ASC");

        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {
             
            ps.setInt(1, idNivel);
            if (idCarrera != null) {
                ps.setInt(2, idCarrera);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GradoCurriculoDTO dto = new GradoCurriculoDTO();
                    dto.setIdGrado(rs.getInt("id_grado"));
                    dto.setNombreGrado(rs.getString("nombre_grado"));
                    dto.setNombreCarrera(rs.getString("nombre_carrera"));
                    dto.setTieneCurriculoActivo(rs.getBoolean("tiene_curriculo"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar grados con estado de currículo: " + e.getMessage());
        }
        return lista;
    }
    
    
    public boolean crearCurriculo(int idGrado, List<Integer> cursosIds) throws SQLException {
        String sqlAnio = "SELECT id_anio_lectivo FROM anio_lectivo WHERE activo = 1 LIMIT 1";
        String sqlInsertCurriculo = "INSERT INTO curriculo (id_grado, id_anio_lectivo) VALUES (?, ?)";
        String sqlInsertCursos = "INSERT INTO curriculo_curso (id_curriculo, id_curso) VALUES (?, ?)";

        Connection conn = null;
        try {
            
            conn = DBConnectionSingleton.getInstance().getConnection();
            
            conn.setAutoCommit(false); 
            int idAnioActivo = -1;
            try (PreparedStatement psAnio = conn.prepareStatement(sqlAnio);
                 ResultSet rsAnio = psAnio.executeQuery()) {
                if (rsAnio.next()) {
                    idAnioActivo = rsAnio.getInt("id_anio_lectivo");
                } else {
                    throw new SQLException("No existe un año lectivo activo en el sistema.");
                }
            }
            int idCurriculo = -1;
            try (PreparedStatement psCur = conn.prepareStatement(sqlInsertCurriculo, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psCur.setInt(1, idGrado);
                psCur.setInt(2, idAnioActivo);
                psCur.executeUpdate();
                
                try (ResultSet rsKeys = psCur.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        idCurriculo = rsKeys.getInt(1);
                    }
                }
            }
            if (idCurriculo == -1) throw new SQLException("Fallo al generar el ID del currículo.");
            try (PreparedStatement psCursos = conn.prepareStatement(sqlInsertCursos)) {
                for (Integer idCurso : cursosIds) {
                    psCursos.setInt(1, idCurriculo);
                    psCursos.setInt(2, idCurso);
                    psCursos.addBatch();
                }
                psCursos.executeBatch();
            }

            conn.commit();
            return true;

            
        } catch (SQLException e) {
            if (conn != null) {
                try { 
                    conn.rollback(); 
                } catch (SQLException ex) {
                    ex.printStackTrace(); 
                }
            }
            if (e.getErrorCode() == 1062) {
                throw new SQLException("Este grado ya cuenta con un currículo para el año activo.");
            }
            throw e;
        } finally {
            if (conn != null) {
                try { 
                    conn.setAutoCommit(true); conn.close(); 
                } catch (SQLException e) { 
                    e.printStackTrace(); 
                }
            }
        }
    }
    
    
    public List<SeccionDTO> listarSeccionesPorGrado(int idGrado) {
        List<SeccionDTO> lista = new ArrayList<>();
        String sql = "SELECT s.id_seccion, s.nombre " +
                     "FROM seccion s " +
                     "JOIN curriculo c ON s.id_curriculo = c.id_curriculo " +
                     "JOIN anio_lectivo al ON c.id_anio_lectivo = al.id_anio_lectivo " +
                     "WHERE c.id_grado = ? AND al.activo = 1 " +
                     "ORDER BY s.nombre ASC";
                     
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, idGrado);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SeccionDTO sec = new SeccionDTO();
                    sec.setIdSeccion(rs.getInt("id_seccion"));
                    sec.setNombre(rs.getString("nombre"));
                    lista.add(sec);
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error al listar secciones: " + e.getMessage());
        }
        return lista;
    }
    
    
    public List<Curso> listarCursosDeCurriculoActivo(int idGrado) {
        List<Curso> lista = new ArrayList<>();
        String sql = "SELECT c.id_curso, c.nombre FROM curso c " +
                     "JOIN curriculo_curso cc ON c.id_curso = cc.id_curso " +
                     "JOIN curriculo cur ON cc.id_curriculo = cur.id_curriculo " +
                     "JOIN anio_lectivo al ON cur.id_anio_lectivo = al.id_anio_lectivo " +
                     "WHERE cur.id_grado = ? AND al.activo = 1 ORDER BY c.nombre ASC";
                     
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Curso curso = new Curso();
                    curso.setIdCurso(rs.getInt("id_curso"));
                    curso.setNombre(rs.getString("nombre"));
                    lista.add(curso);
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    public List<Empleado> listarMaestros() {
        List<Empleado> lista = new ArrayList<>();
        
        String sql = "SELECT e.id_empleado, e.nombres, e.apellidos " +
                     "FROM empleado e " +
                     "JOIN usuario u ON e.id_usuario = u.id_usuario " +
                     "WHERE u.id_rol = 4 " +
                     "ORDER BY e.nombres ASC"; 
                     
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Empleado emp = new Empleado();
                emp.setIdEmpleado(rs.getInt("id_empleado"));
                emp.setNombres(rs.getString("nombres"));
                emp.setApellidos(rs.getString("apellidos"));
                lista.add(emp);
            }
        } catch (java.sql.SQLException e) { 
            System.err.println("Error al listar maestros por rol: " + e.getMessage());
        }
        return lista;
    }


    public boolean crearSeccionConMaestros(CrearSeccionDTO dto) throws SQLException {
        String sqlCur = "SELECT cur.id_curriculo FROM curriculo cur JOIN anio_lectivo al ON cur.id_anio_lectivo = al.id_anio_lectivo WHERE cur.id_grado = ? AND al.activo = 1";
        String sqlSec = "INSERT INTO seccion (id_curriculo, nombre) VALUES (?, ?)"; 
        String sqlAsig = "INSERT INTO asignacion_maestro (id_empleado, id_curso, id_seccion) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnectionSingleton.getInstance().getConnection();
            conn.setAutoCommit(false);

            int idCurriculo = -1;
            try (PreparedStatement psCur = conn.prepareStatement(sqlCur)) {
                psCur.setInt(1, dto.getIdGrado());
                try (java.sql.ResultSet rsCur = psCur.executeQuery()) {
                    if (rsCur.next()) idCurriculo = rsCur.getInt("id_curriculo");
                    else throw new java.sql.SQLException("No se encontró un currículo activo para este grado.");
                }
            }

            int idSeccion = -1;
            try (PreparedStatement psSec = conn.prepareStatement(sqlSec, Statement.RETURN_GENERATED_KEYS)) {
                psSec.setInt(1, idCurriculo);
                psSec.setString(2, dto.getNombreSeccion().trim().toUpperCase());
                psSec.executeUpdate();
                try (ResultSet rsKeys = psSec.getGeneratedKeys()) {
                    if (rsKeys.next()) idSeccion = rsKeys.getInt(1);
                }
            }

            try (PreparedStatement psAsig = conn.prepareStatement(sqlAsig)) {
                for (AsignacionDTO asig : dto.getAsignaciones()) {
                    psAsig.setInt(1, asig.getIdEmpleado());
                    psAsig.setInt(2, asig.getIdCurso());
                    psAsig.setInt(3, idSeccion);
                    psAsig.addBatch();
                }
                psAsig.executeBatch();
            }

            conn.commit();
            return true;

        } catch (java.sql.SQLException e) {
            if (conn != null) try {
                conn.rollback(); 
            } catch (java.sql.SQLException ex) {}
            throw e;
        } finally {
            if (conn != null) try {
                conn.setAutoCommit(true); conn.close();
            } catch (java.sql.SQLException e) {}
        }
    }
}