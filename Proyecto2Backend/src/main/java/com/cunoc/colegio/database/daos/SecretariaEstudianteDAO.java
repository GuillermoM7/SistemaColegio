package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.apirestapp.dto.EstudianteDTO;
import com.cunoc.colegio.apirestapp.dto.ExpedienteAcademicoDTO;
import com.cunoc.colegio.database.DBConnectionSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SecretariaEstudianteDAO {

    public List<EstudianteDTO> buscarEstudiantes(String filtro) {
        List<EstudianteDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM estudiante WHERE nombres LIKE ? OR apellidos LIKE ? ORDER BY nombres ASC LIMIT 20";
        
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String parametro = "%" + filtro + "%";
            ps.setString(1, parametro);
            ps.setString(2, parametro);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EstudianteDTO est = new EstudianteDTO();
                    est.setIdEstudiante(rs.getInt("id_estudiante"));
                    int idUsu = rs.getInt("id_usuario");
                    est.setIdUsuario(rs.wasNull() ? null : idUsu);
                    est.setNombres(rs.getString("nombres"));
                    est.setApellidos(rs.getString("apellidos"));
                    est.setInfoMedica(rs.getString("info_medica"));
                    est.setDatosEncargados(rs.getString("datos_encargados"));
                    est.setEstado(rs.getString("estado"));
                    lista.add(est);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    
    public boolean crearEstudiante(EstudianteDTO dto) {
        String sql = "INSERT INTO estudiante (nombres, apellidos, info_medica, datos_encargados, estado) VALUES (?, ?, ?, ?, 'Activo')";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getNombres().trim());
            ps.setString(2, dto.getApellidos().trim());
            ps.setString(3, dto.getInfoMedica());
            ps.setString(4, dto.getDatosEncargados());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean actualizarEstudiante(EstudianteDTO dto) {
        String sql = "UPDATE estudiante SET nombres=?, apellidos=?, info_medica=?, datos_encargados=? WHERE id_estudiante=?";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getNombres().trim());
            ps.setString(2, dto.getApellidos().trim());
            ps.setString(3, dto.getInfoMedica());
            ps.setString(4, dto.getDatosEncargados());
            ps.setInt(5, dto.getIdEstudiante());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean cambiarEstado(int idEstudiante, String nuevoEstado) {
        String sql = "UPDATE estudiante SET estado = ? WHERE id_estudiante = ?";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idEstudiante);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public List<ExpedienteAcademicoDTO> consultarExpedienteAcademico(int idEstudiante) {
        List<ExpedienteAcademicoDTO> historial = new ArrayList<>();
        String sql = "SELECT al.anio, c.nombre AS curso, n.zona, n.total " +
                     "FROM inscripcion i " +
                     "JOIN anio_lectivo al ON i.id_anio_lectivo = al.id_anio_lectivo " +
                     "JOIN asignacion_maestro am ON i.id_seccion = am.id_seccion " +
                     "JOIN curso c ON am.id_curso = c.id_curso " +
                     "LEFT JOIN nota n ON am.id_asignacion = n.id_asignacion AND n.id_estudiante = i.id_estudiante " +
                     "WHERE i.id_estudiante = ? " +
                     "ORDER BY al.anio DESC, c.nombre ASC";

        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExpedienteAcademicoDTO dto = new ExpedienteAcademicoDTO();
                    dto.setAnioLectivo(rs.getInt("anio"));
                    dto.setNombreCurso(rs.getString("curso"));
                    Double zona = rs.getObject("zona") != null ? rs.getDouble("zona") : null;
                    Double total = rs.getObject("total") != null ? rs.getDouble("total") : null;
                    
                    dto.setZona(zona);
                    dto.setExamenFinal(total);
                    
                    if (zona != null || total != null) {
                        double z = zona != null ? zona : 0;
                        double t = total != null ? total : 0;
                        dto.setNotaTotal(z + t);
                    } else {
                        dto.setNotaTotal(null);
                    }
                    
                    historial.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar expediente: " + e.getMessage());
        }
        return historial;
    }
    
    
    public EstudianteDTO obtenerEstudiantePorUsuario(int idUsuario) {
        String sql = "SELECT id_estudiante, nombres, apellidos, estado FROM estudiante WHERE id_usuario = ?";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EstudianteDTO dto = new EstudianteDTO();
                    dto.setIdEstudiante(rs.getInt("id_estudiante"));
                    dto.setNombres(rs.getString("nombres"));
                    dto.setApellidos(rs.getString("apellidos"));
                    dto.setEstado(rs.getString("estado"));
                    return dto;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estudiante por id_usuario: " + e.getMessage());
        }
        return null;
    }
}