package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.apirestapp.dto.EstadoCuentaResumenDTO;
import com.cunoc.colegio.apirestapp.dto.MovimientoFinancieroDTO;
import com.cunoc.colegio.apirestapp.dto.PerfilEstudianteDTO;
import com.cunoc.colegio.database.DBConnectionSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstudianteOperativoDAO {

    public PerfilEstudianteDTO consultarPerfilCompleto(int idUsuario) {
        String sql = "SELECT e.nombres, e.apellidos, u.correo, " +
                     "n.nombre AS nivel, g.nombre AS grado, s.nombre AS seccion, al.anio " +
                     "FROM estudiante e " +
                     "JOIN usuario u ON e.id_usuario = u.id_usuario " +
                     "JOIN inscripcion i ON e.id_estudiante = i.id_estudiante " +
                     "JOIN seccion s ON i.id_seccion = s.id_seccion " +
                     "JOIN curriculo c ON s.id_curriculo = c.id_curriculo " +
                     "JOIN grado g ON c.id_grado = g.id_grado " +
                     "JOIN nivel n ON g.id_nivel = n.id_nivel " +
                     "JOIN anio_lectivo al ON i.id_anio_lectivo = al.id_anio_lectivo " +
                     "WHERE e.id_usuario = ? " +
                     "ORDER BY al.anio DESC LIMIT 1";

        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PerfilEstudianteDTO dto = new PerfilEstudianteDTO();
                    dto.setNombres(rs.getString("nombres"));
                    dto.setApellidos(rs.getString("apellidos"));
                    dto.setCorreo(rs.getString("correo"));
                    dto.setNivelAcademico(rs.getString("nivel"));
                    dto.setGrado(rs.getString("grado"));
                    dto.setSeccion(rs.getString("seccion"));
                    dto.setAnioActual(rs.getInt("anio"));
                    return dto;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al consultar perfil del estudiante: " + ex.getMessage());
        }
        return null;
    }
    
    
    public EstadoCuentaResumenDTO consultarEstadoCuenta(int idUsuario) {
        EstadoCuentaResumenDTO resumen = new EstadoCuentaResumenDTO();
        java.util.List<MovimientoFinancieroDTO> movimientos = new java.util.ArrayList<>();
        double saldoTotal = 0.0;

        String sql = "(SELECT 'CARGO ACADÉMICO' AS tipo, c.concepto, c.mes_referencia, " +
                     "c.monto_cobrado AS monto_adeudado, p.monto AS monto_pagado, " +
                     "p.fecha_pago, c.estado_pago AS estado " +
                     "FROM cargo c " +
                     "JOIN estudiante e ON c.id_estudiante = e.id_estudiante " +
                     "LEFT JOIN pago p ON c.id_cargo = p.id_cargo " +
                     "WHERE e.id_usuario = ?) " +
                     "UNION ALL " +
                     "(SELECT 'MULTA BIBLIOTECA' AS tipo, 'Atraso en devolución' AS concepto, NULL AS mes_referencia, " +
                     "m.monto AS monto_adeudado, p.monto AS monto_pagado, " +
                     "p.fecha_pago, m.estado AS estado " +
                     "FROM multa m " +
                     "JOIN prestamo pr ON m.id_prestamo = pr.id_prestamo " +
                     "LEFT JOIN pago p ON m.id_multa = p.id_multa " +
                     "WHERE pr.id_usuario = ?) " +
                     "ORDER BY estado DESC, fecha_pago DESC";

        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {            
            // Asignamos el idUsuario a ambas partes del UNION ALL
            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MovimientoFinancieroDTO mov = new MovimientoFinancieroDTO();
                    mov.setTipo(rs.getString("tipo"));
                    mov.setConcepto(rs.getString("concepto"));
                    
                    Object mes = rs.getObject("mes_referencia");
                    mov.setMesReferencia(mes != null ? (Integer) mes : null);
                    
                    double montoAdeudado = rs.getDouble("monto_adeudado");
                    mov.setMontoAdeudado(montoAdeudado);
                    
                    Object montoPagado = rs.getObject("monto_pagado");
                    mov.setMontoPagado(montoPagado != null ? ((Number) montoPagado).doubleValue() : null);
                    
                    java.sql.Timestamp fechaPago = rs.getTimestamp("fecha_pago");
                    mov.setFechaPago(fechaPago != null ? fechaPago.toString() : null);
                    
                    String estado = rs.getString("estado");
                    mov.setEstado(estado);
                    
                    if ("PENDIENTE".equalsIgnoreCase(estado)) {
                        saldoTotal += montoAdeudado;
                    }
                    
                    movimientos.add(mov);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar estado de cuenta: " + e.getMessage());
        }
        
        resumen.setMovimientos(movimientos);
        resumen.setSaldoTotal(saldoTotal);
        return resumen;
    }
}