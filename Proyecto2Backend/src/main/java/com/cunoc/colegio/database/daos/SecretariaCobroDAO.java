package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.apirestapp.dto.CargoDTO;
import com.cunoc.colegio.apirestapp.dto.PagoDTO;
import com.cunoc.colegio.apirestapp.dto.RegistrarPagoDTO;
import com.cunoc.colegio.database.DBConnectionSingleton;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SecretariaCobroDAO {

    
    public List<CargoDTO> listarCargosPendientes(int idEstudiante) {
        List<CargoDTO> lista = new ArrayList<>();
        String sql = "SELECT id_cargo, concepto, mes_referencia, monto_cobrado, estado_pago, DATE_FORMAT(fecha_generacion, '%d/%m/%Y') AS fecha " +
                     "FROM cargo WHERE id_estudiante = ? AND estado_pago = 'Pendiente' ORDER BY mes_referencia ASC, fecha_generacion ASC";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CargoDTO c = new CargoDTO();
                    c.setIdCargo(rs.getInt("id_cargo"));
                    c.setConcepto(rs.getString("concepto"));
                    int mes = rs.getInt("mes_referencia");
                    c.setMesReferencia(rs.wasNull() ? null : mes);
                    c.setMontoCobrado(rs.getDouble("monto_cobrado"));
                    c.setEstadoPago(rs.getString("estado_pago"));
                    c.setFechaGeneracion(rs.getString("fecha"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    
    public boolean procesarPagoCargo(RegistrarPagoDTO dto) throws SQLException {
        String sqlInfoCargo = "SELECT concepto, mes_referencia, monto_cobrado, estado_pago FROM cargo WHERE id_cargo = ? AND id_estudiante = ?";
        String sqlUpdateCargo = "UPDATE cargo SET estado_pago = 'Pagado' WHERE id_cargo = ?";
        String sqlInsertPago = "INSERT INTO pago (id_estudiante, id_cargo, concepto, descripcion, monto, fecha_pago, id_secretaria) VALUES (?, ?, ?, ?, ?, NOW(), ?)";

        Connection conn = null;
        try {
            conn = DBConnectionSingleton.getInstance().getConnection();
            conn.setAutoCommit(false);

            String concepto = "";
            Integer mesRef = null;
            double monto = 0;
            
            try (PreparedStatement psInfo = conn.prepareStatement(sqlInfoCargo)) {
                psInfo.setInt(1, dto.getIdCargo());
                psInfo.setInt(2, dto.getIdEstudiante());
                try (ResultSet rs = psInfo.executeQuery()) {
                    if (rs.next()) {
                        if (rs.getString("estado_pago").equals("Pagado")) throw new SQLException("El cargo ya se encuentra pagado.");
                        concepto = rs.getString("concepto");
                        mesRef = rs.getInt("mes_referencia");
                        if (rs.wasNull()) mesRef = null;
                        monto = rs.getDouble("monto_cobrado");
                    } else {
                        throw new SQLException("Cargo no encontrado o no pertenece al estudiante.");
                    }
                }
            }

            String descripcion = concepto;
            if (mesRef != null) {
                String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
                descripcion += " - " + (mesRef > 0 && mesRef <= 12 ? meses[mesRef] : "Mes " + mesRef);
            }

            try (PreparedStatement psUpd = conn.prepareStatement(sqlUpdateCargo)) {
                psUpd.setInt(1, dto.getIdCargo());
                psUpd.executeUpdate();
            }

            try (PreparedStatement psIns = conn.prepareStatement(sqlInsertPago)) {
                psIns.setInt(1, dto.getIdEstudiante());
                psIns.setInt(2, dto.getIdCargo());
                psIns.setString(3, concepto);
                psIns.setString(4, descripcion);
                psIns.setDouble(5, monto);
                psIns.setInt(6, dto.getIdSecretaria());
                psIns.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            throw e;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) {}
        }
    }
    
    
    public List<PagoDTO> listarHistorialPagos(int idEstudiante) {
        List<PagoDTO> lista = new ArrayList<>();
        String sql = "SELECT id_pago, concepto, descripcion, monto, DATE_FORMAT(fecha_pago, '%d/%m/%Y %H:%i') AS fecha " +
                     "FROM pago WHERE id_estudiante = ? ORDER BY fecha_pago DESC";
                     
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PagoDTO p = new PagoDTO();
                    p.setIdPago(rs.getInt("id_pago"));
                    p.setConcepto(rs.getString("concepto"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setMonto(rs.getDouble("monto"));
                    p.setFechaPago(rs.getString("fecha"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) { 
            System.err.println("Error al obtener historial: " + e.getMessage());
        }
        return lista;
    }
    
    
    public byte[] generarReciboPDF(int idPago) {
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection()) {
            
            InputStream reportStream = getClass().getResourceAsStream("/reportes/recibo.jrxml");
            if (reportStream == null) {
                throw new RuntimeException("No se encontró el archivo de reporte recibo.jrxml");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id_pago", idPago);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            System.err.println("Error al generar PDF con JasperReports: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    
    public byte[] generarSolvenciaPDF(int idEstudiante) {
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection()) {

            String sqlCheck = "SELECT COUNT(*) AS deudas FROM cargo WHERE id_estudiante = ? AND estado_pago = 'Pendiente'";
            try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
                ps.setInt(1, idEstudiante);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt("deudas") > 0) {
                        throw new Exception("El estudiante tiene cargos pendientes, no se puede emitir solvencia.");
                    }
                }
            }

            InputStream reportStream = getClass().getResourceAsStream("/reportes/solvencia.jrxml");
            if (reportStream == null) throw new RuntimeException("No se encontró solvencia.jrxml");

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id_estudiante", idEstudiante);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            System.err.println("Error al generar Solvencia: " + e.getMessage());
            return null;
        }
    }
}