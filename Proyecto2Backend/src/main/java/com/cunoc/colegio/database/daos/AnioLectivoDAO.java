package com.cunoc.colegio.database.daos; 

import com.cunoc.colegio.database.DBConnectionSingleton;
import com.cunoc.colegio.database.entidades.AnioLectivo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnioLectivoDAO {

    
    public List<AnioLectivo> listarAnios() {
        List<AnioLectivo> lista = new ArrayList<>();
        String sql = "SELECT * FROM anio_lectivo ORDER BY anio DESC";

        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new AnioLectivo(
                        rs.getInt("id_anio_lectivo"),
                        rs.getInt("anio"),
                        rs.getString("fecha_inicio"),
                        rs.getString("fecha_fin"),
                        rs.getBoolean("activo")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar años lectivos: " + e.getMessage());
        }
        return lista;
    }


    public boolean crearAnio(AnioLectivo anio) {
        String sql = "INSERT INTO anio_lectivo (anio, fecha_inicio, fecha_fin, activo) VALUES (?, ?, ?, ?)";
        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, anio.getAnio());
            ps.setString(2, anio.getFechaInicio());
            ps.setString(3, anio.getFechaFin());
            ps.setBoolean(4, anio.isActivo());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al crear año lectivo: " + e.getMessage());
            return false;
        }
    }


    public boolean activarAnioLectivo(int idAnioActivar) {
        Connection conexion = null;
        PreparedStatement psDesactivarTodos = null;
        PreparedStatement psActivarUno = null;

        try {
            conexion = DBConnectionSingleton.getInstance().getConnection();
            conexion.setAutoCommit(false);

            String sqlApagar = "UPDATE anio_lectivo SET activo = false";
            psDesactivarTodos = conexion.prepareStatement(sqlApagar);
            psDesactivarTodos.executeUpdate();

            String sqlEncender = "UPDATE anio_lectivo SET activo = true WHERE id_anio_lectivo = ?";
            psActivarUno = conexion.prepareStatement(sqlEncender);
            psActivarUno.setInt(1, idAnioActivar);
            psActivarUno.executeUpdate();

            conexion.commit();
            return true;

        } catch (SQLException e) {
            if (conexion != null) {
                try { conexion.rollback(); } catch (SQLException ex) { }
            }
            System.err.println("Error en transacción al activar año: " + e.getMessage());
            return false;
        } finally {
            try {
                if (psDesactivarTodos != null) psDesactivarTodos.close(); 
            } catch (SQLException e) { }
            try {
                if (psActivarUno != null) psActivarUno.close(); 
            } catch (SQLException e) { }
            try { 
                if (conexion != null) {
                    conexion.setAutoCommit(true); conexion.close(); 
                } 
            } catch (SQLException e) { }
        }
    }
}