package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.apirestapp.dto.LibroDTO;
import com.cunoc.colegio.apirestapp.dto.MultaUsuarioDTO;
import com.cunoc.colegio.apirestapp.dto.PrestamoInfoDTO;
import com.cunoc.colegio.apirestapp.dto.PrestamoUsuarioDTO;
import com.cunoc.colegio.apirestapp.dto.ReporteMoraDTO;
import com.cunoc.colegio.apirestapp.dto.UsuarioBusquedaDTO;
import com.cunoc.colegio.database.DBConnectionSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BibliotecaDAO {

    
    public boolean crearLibro(LibroDTO libro) {
        String sql = "INSERT INTO libro (titulo, autor, editorial, cantidad_total, cantidad_disponible) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getEditorial());
            ps.setInt(4, libro.getCantidadTotal());
            ps.setInt(5, libro.getCantidadDisponible());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear libro: " + e.getMessage());
            return false;
        }
    }

    
    public List<LibroDTO> listarLibros() {
        List<LibroDTO> lista = new ArrayList<>();
        String sql = "SELECT id_libro, titulo, autor, editorial, cantidad_total, cantidad_disponible FROM libro ORDER BY titulo ASC";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LibroDTO dto = new LibroDTO();
                dto.setIdLibro(rs.getInt("id_libro"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setAutor(rs.getString("autor"));
                dto.setEditorial(rs.getString("editorial"));
                dto.setCantidadTotal(rs.getInt("cantidad_total"));
                dto.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                lista.add(dto);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar libros: " + e.getMessage());
        }
        return lista;
    }

    
    public boolean actualizarLibro(LibroDTO libro) {
        String sql = "UPDATE libro SET titulo = ?, autor = ?, editorial = ?, cantidad_total = ?, cantidad_disponible = ? WHERE id_libro = ?";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getEditorial());
            ps.setInt(4, libro.getCantidadTotal());
            ps.setInt(5, libro.getCantidadDisponible());
            ps.setInt(6, libro.getIdLibro());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            return false;
        }
    }
    
    
    public boolean eliminarLibro(int idLibro) {
        String sql = "DELETE FROM libro WHERE id_libro = ?";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLibro);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("No se puede eliminar, el libro tiene historial de préstamos: " + e.getMessage());
            return false;
        }
    }
    
    
    public List<UsuarioBusquedaDTO> buscarUsuariosParaPrestamo(String termino) {
        List<UsuarioBusquedaDTO> lista = new ArrayList<>();
        String query = "%" + termino + "%";

        String sql = "SELECT u.id_usuario, u.correo, " +
                     "COALESCE(e.nombres, emp.nombres) AS nombres, " +
                     "COALESCE(e.apellidos, emp.apellidos) AS apellidos, " +
                     "CASE WHEN u.id_rol = 5 THEN 'Estudiante' ELSE 'Empleado' END as tipo " +
                     "FROM usuario u " +
                     "LEFT JOIN estudiante e ON u.id_usuario = e.id_usuario " +
                     "LEFT JOIN empleado emp ON u.id_usuario = emp.id_usuario " +
                     "WHERE u.activo = 1 AND u.id_rol IN (4, 5) " +
                     "AND (e.nombres LIKE ? OR e.apellidos LIKE ? OR emp.nombres LIKE ? OR emp.apellidos LIKE ? OR u.correo LIKE ?) " +
                     "LIMIT 10";

        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for(int i = 1; i <= 5; i++) ps.setString(i, query);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UsuarioBusquedaDTO dto = new UsuarioBusquedaDTO();
                    dto.setIdUsuario(rs.getInt("id_usuario"));
                    dto.setCorreo(rs.getString("correo"));
                    dto.setNombreCompleto(rs.getString("apellidos") + ", " + rs.getString("nombres"));
                    dto.setTipoUsuario(rs.getString("tipo"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuarios: " + e.getMessage());
        }
        return lista;
    }

    
    public List<PrestamoInfoDTO> listarPrestamosActivosPorLibro(int idLibro) {
        List<PrestamoInfoDTO> lista = new ArrayList<>();
        String sql = "SELECT p.id_prestamo, p.id_usuario, u.correo, " +
                     "COALESCE(e.nombres, emp.nombres) AS nombres, " +
                     "COALESCE(e.apellidos, emp.apellidos) AS apellidos, " +
                     "p.fecha_prestamo, p.fecha_esperada_devolucion " +
                     "FROM prestamo p " +
                     "JOIN usuario u ON p.id_usuario = u.id_usuario " +
                     "LEFT JOIN estudiante e ON u.id_usuario = e.id_usuario " +
                     "LEFT JOIN empleado emp ON u.id_usuario = emp.id_usuario " +
                     "WHERE p.id_libro = ? AND p.fecha_devolucion_real IS NULL";
                     
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLibro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PrestamoInfoDTO dto = new PrestamoInfoDTO();
                    dto.setIdPrestamo(rs.getInt("id_prestamo"));
                    dto.setIdUsuario(rs.getInt("id_usuario"));
                    dto.setCorreo(rs.getString("correo"));
                    dto.setNombreCompleto(rs.getString("apellidos") + ", " + rs.getString("nombres"));
                    dto.setFechaPrestamo(rs.getDate("fecha_prestamo") != null ? rs.getDate("fecha_prestamo").toString() : null);
                    dto.setFechaEsperada(rs.getDate("fecha_esperada_devolucion") != null ? rs.getDate("fecha_esperada_devolucion").toString() : null);
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar préstamos: " + e.getMessage());
        }
        return lista;
    }

    
    public boolean registrarPrestamo(int idLibro, int idUsuario, java.sql.Date fechaEsperada) {
        String insertPrestamo = "INSERT INTO prestamo (id_libro, id_usuario, fecha_prestamo, fecha_esperada_devolucion) VALUES (?, ?, CURRENT_DATE, ?)";
        String updateInventario = "UPDATE libro SET cantidad_disponible = cantidad_disponible - 1 WHERE id_libro = ? AND cantidad_disponible > 0";
        
        
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection()) {
            conn.setAutoCommit(false); 
            try (PreparedStatement psUpdate = conn.prepareStatement(updateInventario)) {
                psUpdate.setInt(1, idLibro);
                int filasAfectadas = psUpdate.executeUpdate();
                if (filasAfectadas == 0) {
                    conn.rollback(); 
                    return false;
                }
            }
            
            try (PreparedStatement psInsert = conn.prepareStatement(insertPrestamo)) {
                psInsert.setInt(1, idLibro);
                psInsert.setInt(2, idUsuario);
                psInsert.setDate(3, fechaEsperada);
                psInsert.executeUpdate();
            }
            
            conn.commit(); 
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error en transacción de préstamo: " + e.getMessage());
            return false;
        }
    }
    
    
    public List<PrestamoUsuarioDTO> listarPrestamosPorUsuario(int idUsuario) {
        List<PrestamoUsuarioDTO> lista = new ArrayList<>();
        String sql = "SELECT p.id_prestamo, p.id_libro, l.titulo, p.fecha_prestamo, p.fecha_esperada_devolucion " +
                     "FROM prestamo p JOIN libro l ON p.id_libro = l.id_libro " +
                     "WHERE p.id_usuario = ? AND p.fecha_devolucion_real IS NULL";
        
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                java.time.LocalDate hoy = java.time.LocalDate.now();
                
                while (rs.next()) {
                    PrestamoUsuarioDTO dto = new PrestamoUsuarioDTO();
                    dto.setIdPrestamo(rs.getInt("id_prestamo"));
                    dto.setIdLibro(rs.getInt("id_libro"));
                    dto.setTituloLibro(rs.getString("titulo"));
                    
                    java.sql.Date fechaPrestamo = rs.getDate("fecha_prestamo");
                    java.sql.Date fechaEsperada = rs.getDate("fecha_esperada_devolucion");
                    
                    dto.setFechaPrestamo(fechaPrestamo != null ? fechaPrestamo.toString() : null);
                    dto.setFechaEsperada(fechaEsperada != null ? fechaEsperada.toString() : null);

                    if (fechaEsperada != null) {
                        java.time.LocalDate esperadaLocal = fechaEsperada.toLocalDate();
                        long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(esperadaLocal, hoy);
                        int atraso = (int) Math.max(0, diasAtraso);
                        dto.setDiasAtraso(atraso);
                        dto.setMontoMulta(atraso * 5.0); // Tarifa
                    }
                    
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar préstamos: " + e.getMessage());
        }
        return lista;
        }

    
    public boolean registrarDevolucion(int idPrestamo, int idLibro, double montoMulta) {
        String updatePrestamo = "UPDATE prestamo SET fecha_devolucion_real = CURRENT_DATE WHERE id_prestamo = ?";
        String updateInventario = "UPDATE libro SET cantidad_disponible = cantidad_disponible + 1 WHERE id_libro = ?";
        String insertMulta = "INSERT INTO multa (id_prestamo, monto, estado) VALUES (?, ?, 'PENDIENTE')";
        
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(updatePrestamo)) {
                ps1.setInt(1, idPrestamo);
                ps1.executeUpdate();
            }
            
            try (PreparedStatement ps2 = conn.prepareStatement(updateInventario)) {
                ps2.setInt(1, idLibro);
                ps2.executeUpdate();
            }

            if (montoMulta > 0) {
                try (PreparedStatement ps3 = conn.prepareStatement(insertMulta)) {
                    ps3.setInt(1, idPrestamo);
                    ps3.setDouble(2, montoMulta);
                    ps3.executeUpdate();
                }
            }           
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error en transacción de devolución: " + e.getMessage());
            return false;
        }
    }
    
    
    public List<MultaUsuarioDTO> listarMultasPorUsuario(int idUsuario) {
        List<MultaUsuarioDTO> lista = new ArrayList<>();
        String sql = "SELECT m.id_multa, l.titulo, p.fecha_devolucion_real, m.monto, m.estado " +
                     "FROM multa m " +
                     "JOIN prestamo p ON m.id_prestamo = p.id_prestamo " +
                     "JOIN libro l ON p.id_libro = l.id_libro " +
                     "WHERE p.id_usuario = ? ORDER BY m.id_multa DESC";
                     
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MultaUsuarioDTO dto = new MultaUsuarioDTO();
                    dto.setIdMulta(rs.getInt("id_multa"));
                    dto.setTituloLibro(rs.getString("titulo"));
                    dto.setFechaDevolucionReal(rs.getDate("fecha_devolucion_real") != null ? rs.getDate("fecha_devolucion_real").toString() : "");
                    dto.setMonto(rs.getDouble("monto"));
                    dto.setEstado(rs.getString("estado"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar multas del usuario: " + e.getMessage());
        }
        return lista;
    }


    public List<ReporteMoraDTO> obtenerReportePrestamosEnMora() {
        List<ReporteMoraDTO> lista = new ArrayList<>();

        String sql = "SELECT l.titulo, e.nombres, e.apellidos, p.fecha_esperada_devolucion " +
                     "FROM prestamo p " +
                     "JOIN libro l ON p.id_libro = l.id_libro " +
                     "JOIN estudiante e ON p.id_usuario = e.id_usuario " + 
                     "WHERE p.fecha_devolucion_real IS NULL AND p.fecha_esperada_devolucion < CURRENT_DATE " +
                     "ORDER BY p.fecha_esperada_devolucion ASC";
                     
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            java.time.LocalDate hoy = java.time.LocalDate.now();
            
            while (rs.next()) {
                ReporteMoraDTO dto = new ReporteMoraDTO();
                dto.setTituloLibro(rs.getString("titulo"));
                dto.setEstudiantePoseedor(rs.getString("apellidos") + ", " + rs.getString("nombres"));
                
                java.sql.Date fechaEsperada = rs.getDate("fecha_esperada_devolucion");
                dto.setFechaEsperada(fechaEsperada.toString());

                java.time.LocalDate esperadaLocal = fechaEsperada.toLocalDate();
                long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(esperadaLocal, hoy);
                int atraso = (int) diasAtraso;
                
                dto.setDiasRetraso(atraso);
                dto.setRecargoFinanciero(atraso * 5.0); // Tarifa Q5 base
                
                lista.add(dto);
            }
        } catch (SQLException e) {
            System.err.println("Error al generar reporte de mora: " + e.getMessage());
        }
        return lista;
    }
    
}