package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.apirestapp.dto.InscripcionDTO;
import com.cunoc.colegio.database.DBConnectionSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SecretariaInscripcionDAO {

    public String procesarInscripcion(InscripcionDTO dto) throws SQLException {
        String sqlAnio = "SELECT id_anio_lectivo FROM anio_lectivo WHERE activo = 1 LIMIT 1";
        
        String sqlVerificar = "SELECT 1 FROM inscripcion WHERE id_estudiante = ? AND id_anio_lectivo = ?";
        
        String sqlPrecios = "SELECT n.precio_inscripcion, n.precio_colegiatura FROM seccion s " +
                            "JOIN curriculo c ON s.id_curriculo = c.id_curriculo " +
                            "JOIN grado g ON c.id_grado = g.id_grado " +
                            "JOIN nivel n ON g.id_nivel = n.id_nivel WHERE s.id_seccion = ?";

        String sqlInscribir = "INSERT INTO inscripcion (id_estudiante, id_seccion, fecha_inscripcion, id_anio_lectivo) VALUES (?, ?, CURDATE(), ?)";
        
        String sqlCargoInscripcion = "INSERT INTO cargo (id_estudiante, id_anio_lectivo, concepto, monto_cobrado, estado_pago) VALUES (?, ?, 'Inscripcion', ?, 'Pendiente')";
        
        String sqlCargoMensualidad = "INSERT INTO cargo (id_estudiante, id_anio_lectivo, concepto, mes_referencia, monto_cobrado, estado_pago) VALUES (?, ?, 'Mes colegiatura', ?, ?, 'Pendiente')";

        Connection conn = null;
        try {
            conn = DBConnectionSingleton.getInstance().getConnection();
            conn.setAutoCommit(false);

            int idAnioActivo = -1;
            try (PreparedStatement psAnio = conn.prepareStatement(sqlAnio); ResultSet rsAnio = psAnio.executeQuery()) {
                if (rsAnio.next()) idAnioActivo = rsAnio.getInt("id_anio_lectivo");
                else throw new SQLException("No hay un año lectivo activo configurado.");
            }

            try (PreparedStatement psVer = conn.prepareStatement(sqlVerificar)) {
                psVer.setInt(1, dto.getIdEstudiante());
                psVer.setInt(2, idAnioActivo);
                try (ResultSet rsVer = psVer.executeQuery()) {
                    if (rsVer.next()) throw new SQLException("El estudiante ya está inscrito en el ciclo actual.");
                }
            }

            double precioInscripcion = 0;
            double precioColegiatura = 0;
            try (PreparedStatement psPrecios = conn.prepareStatement(sqlPrecios)) {
                psPrecios.setInt(1, dto.getIdSeccion());
                try (ResultSet rsPrecios = psPrecios.executeQuery()) {
                    if (rsPrecios.next()) {
                        precioInscripcion = rsPrecios.getDouble("precio_inscripcion");
                        precioColegiatura = rsPrecios.getDouble("precio_colegiatura");
                    } else {
                        throw new SQLException("No se encontraron los precios configurados para este grado/nivel.");
                    }
                }
            }

            try (PreparedStatement psInscribir = conn.prepareStatement(sqlInscribir)) {
                psInscribir.setInt(1, dto.getIdEstudiante());
                psInscribir.setInt(2, dto.getIdSeccion());
                psInscribir.setInt(3, idAnioActivo);
                psInscribir.executeUpdate();
            }

            try (PreparedStatement psCargoIns = conn.prepareStatement(sqlCargoInscripcion)) {
                psCargoIns.setInt(1, dto.getIdEstudiante());
                psCargoIns.setInt(2, idAnioActivo);
                psCargoIns.setDouble(3, precioInscripcion);
                psCargoIns.executeUpdate();
            }

            try (PreparedStatement psCargoMensual = conn.prepareStatement(sqlCargoMensualidad)) {
                for (int mes = 1; mes <= 10; mes++) {
                    psCargoMensual.setInt(1, dto.getIdEstudiante());
                    psCargoMensual.setInt(2, idAnioActivo);
                    psCargoMensual.setInt(3, mes);
                    psCargoMensual.setDouble(4, precioColegiatura);
                    psCargoMensual.addBatch();
                }
                psCargoMensual.executeBatch();
            }

            conn.commit();
            return "Estudiante inscrito exitosamente. Cargos del año generados.";

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            throw e;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) {}
        }
    }
}