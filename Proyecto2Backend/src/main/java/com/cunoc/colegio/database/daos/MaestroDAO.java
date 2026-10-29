package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.apirestapp.dto.ActividadDTO;
import com.cunoc.colegio.apirestapp.dto.AlumnoNominaDTO;
import com.cunoc.colegio.apirestapp.dto.AsistenciaDTO;
import com.cunoc.colegio.apirestapp.dto.CursoAsignadoDTO;
import com.cunoc.colegio.apirestapp.dto.NotaActividadDTO;
import com.cunoc.colegio.apirestapp.dto.NotaExamenDTO;
import com.cunoc.colegio.database.DBConnectionSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaestroDAO {

    
    public List<CursoAsignadoDTO> listarCursosAsignados(int idUsuario) {
        List<CursoAsignadoDTO> lista = new ArrayList<>();
        
        String sql = "SELECT am.id_asignacion, c.id_curso, c.nombre AS curso, " +
                     "s.id_seccion, s.nombre AS seccion, g.nombre AS grado " +
                     "FROM asignacion_maestro am " +
                     "JOIN empleado e ON am.id_empleado = e.id_empleado " +
                     "JOIN curso c ON am.id_curso = c.id_curso " +
                     "JOIN seccion s ON am.id_seccion = s.id_seccion " +
                     "JOIN curriculo cur ON s.id_curriculo = cur.id_curriculo " +
                     "JOIN grado g ON cur.id_grado = g.id_grado " +
                     "JOIN anio_lectivo al ON cur.id_anio_lectivo = al.id_anio_lectivo " +
                     "WHERE e.id_usuario = ? AND al.activo = 1 " +
                     "ORDER BY g.id_grado ASC, s.nombre ASC";

        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, idUsuario); 
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CursoAsignadoDTO dto = new CursoAsignadoDTO();
                    dto.setIdAsignacion(rs.getInt("id_asignacion"));
                    dto.setIdCurso(rs.getInt("id_curso"));
                    dto.setNombreCurso(rs.getString("curso"));
                    dto.setIdSeccion(rs.getInt("id_seccion"));
                    dto.setNombreSeccion(rs.getString("seccion"));
                    dto.setNombreGrado(rs.getString("grado"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar cursos asignados: " + e.getMessage());
        }
        return lista;
    }
    
    
    public List<AlumnoNominaDTO> listarNominaAlumnos(int idAsignacion) {
        List<AlumnoNominaDTO> lista = new ArrayList<>();        
        String sql = "SELECT e.id_estudiante, e.nombres, e.apellidos, n.zona, n.total " +
                     "FROM asignacion_maestro am " +
                     "JOIN inscripcion i ON am.id_seccion = i.id_seccion " +
                     "JOIN estudiante e ON i.id_estudiante = e.id_estudiante " +
                     "LEFT JOIN nota n ON am.id_asignacion = n.id_asignacion AND e.id_estudiante = n.id_estudiante " +
                     "WHERE am.id_asignacion = ? " +
                     "ORDER BY e.apellidos ASC, e.nombres ASC";

        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, idAsignacion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AlumnoNominaDTO dto = new AlumnoNominaDTO();
                    dto.setIdEstudiante(rs.getInt("id_estudiante"));
                    dto.setNombres(rs.getString("nombres"));
                    dto.setApellidos(rs.getString("apellidos"));                   
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
                    
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar nómina: " + e.getMessage());
        }
        return lista;
    }
    
    
    public boolean registrarAsistenciaMasiva(List<AsistenciaDTO> listaAsistencia) {
        if (listaAsistencia == null || listaAsistencia.isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO asistencia (id_estudiante, id_asignacion, fecha, estado) " +
                     "VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE estado = VALUES(estado)";

        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             

            conn.setAutoCommit(false);
            
            for (AsistenciaDTO asistencia : listaAsistencia) {
                ps.setInt(1, asistencia.getIdEstudiante());
                ps.setInt(2, asistencia.getIdAsignacion());
                ps.setDate(3, java.sql.Date.valueOf(asistencia.getFecha())); 
                ps.setString(4, asistencia.getEstado());
                
                ps.addBatch();
            }
            
            ps.executeBatch();
            conn.commit();    
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error en Batch de asistencia: " + e.getMessage());
            try (Connection conn = DBConnectionSingleton.getInstance().getConnection()) {
                 conn.rollback();
            } catch (SQLException ex) {}
            return false;
        }
    }
    
    
    public boolean crearActividad(ActividadDTO actividad) {
        String sql = "INSERT INTO actividad (id_asignacion, nombre_actividad, ponderacion) VALUES (?, ?, ?)";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, actividad.getIdAsignacion());
            ps.setString(2, actividad.getNombreActividad());
            ps.setDouble(3, actividad.getPonderacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear actividad: " + e.getMessage());
            return false;
        }
    }

    
    public List<ActividadDTO> listarActividades(int idAsignacion) {
        List<ActividadDTO> lista = new ArrayList<>();
        String sql = "SELECT id_actividad, nombre_actividad, ponderacion FROM actividad WHERE id_asignacion = ?";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAsignacion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ActividadDTO dto = new ActividadDTO();
                    dto.setIdActividad(rs.getInt("id_actividad"));
                    dto.setIdAsignacion(idAsignacion);
                    dto.setNombreActividad(rs.getString("nombre_actividad"));
                    dto.setPonderacion(rs.getDouble("ponderacion"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar actividades: " + e.getMessage());
        }
        return lista;
    }

    
    public boolean calificarActividadMasiva(int idAsignacion, List<NotaActividadDTO> notas) {
        if (notas == null || notas.isEmpty()) return false;

        String sqlInsertNotas = "INSERT INTO nota_actividad (id_actividad, id_estudiante, punteo_obtenido) " +
                                "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE punteo_obtenido = VALUES(punteo_obtenido)";
        
        String sqlUpdateZona = "INSERT INTO nota (id_estudiante, id_asignacion, zona) " +
                               "SELECT na.id_estudiante, a.id_asignacion, SUM(na.punteo_obtenido) " +
                               "FROM nota_actividad na " +
                               "JOIN actividad a ON na.id_actividad = a.id_actividad " +
                               "WHERE a.id_asignacion = ? " +
                               "GROUP BY na.id_estudiante, a.id_asignacion " +
                               "ON DUPLICATE KEY UPDATE zona = VALUES(zona)";

        try (Connection conn = DBConnectionSingleton.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psNotas = conn.prepareStatement(sqlInsertNotas)) {
                for (NotaActividadDTO nota : notas) {
                    psNotas.setInt(1, nota.getIdActividad());
                    psNotas.setInt(2, nota.getIdEstudiante());
                    psNotas.setDouble(3, nota.getPunteoObtenido());
                    psNotas.addBatch();
                }
                psNotas.executeBatch();
            }
            
            try (PreparedStatement psZona = conn.prepareStatement(sqlUpdateZona)) {
                psZona.setInt(1, idAsignacion);
                psZona.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en Batch de calificaciones: " + e.getMessage());
            return false;
        }
    }
    
    
    public List<NotaActividadDTO> obtenerNotasPorActividad(int idActividad) {
        List<NotaActividadDTO> lista = new ArrayList<>();
        String sql = "SELECT id_estudiante, punteo_obtenido FROM nota_actividad WHERE id_actividad = ?";
        
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, idActividad);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NotaActividadDTO dto = new NotaActividadDTO();
                    dto.setIdEstudiante(rs.getInt("id_estudiante"));
                    dto.setIdActividad(idActividad);
                    dto.setPunteoObtenido(rs.getDouble("punteo_obtenido"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener notas de actividad: " + e.getMessage());
        }
        return lista;
    }
    
    
    public List<NotaExamenDTO> obtenerNotasExamenFinal(int idAsignacion) {
        List<NotaExamenDTO> lista = new ArrayList<>();
        String sql = "SELECT id_estudiante, total FROM nota WHERE id_asignacion = ? AND total IS NOT NULL";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAsignacion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NotaExamenDTO dto = new NotaExamenDTO();
                    dto.setIdEstudiante(rs.getInt("id_estudiante"));
                    dto.setNotaExamen(rs.getDouble("total"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar examen final: " + e.getMessage());
        }
        return lista;
    }

    
    public boolean calificarExamenFinalMasivo(int idAsignacion, List<NotaExamenDTO> notas) {
        if (notas == null || notas.isEmpty()) return false;

        String sql = "INSERT INTO nota (id_estudiante, id_asignacion, total) " +
                     "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE total = VALUES(total)";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (NotaExamenDTO nota : notas) {
                    ps.setInt(1, nota.getIdEstudiante());
                    ps.setInt(2, idAsignacion);
                    ps.setDouble(3, nota.getNotaExamen());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al guardar examen final: " + e.getMessage());
            return false;
        }
    }
    
}