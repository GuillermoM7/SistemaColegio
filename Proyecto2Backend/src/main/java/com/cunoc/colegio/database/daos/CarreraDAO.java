package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.database.DBConnectionSingleton;
import com.cunoc.colegio.database.entidades.Carrera;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarreraDAO {
    
    
    public boolean crearCarrera(Carrera carrera) {
        String sql = "INSERT INTO Carrera (nombre, activa) VALUES (?, ?)";
        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
             
            ps.setString(1, carrera.getNombre());
            ps.setBoolean(2, true); 
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear carrera: " + e.getMessage());
            return false;
        }
    }
    
    
    public List<Carrera> listarCarreras() {
        List<Carrera> lista = new ArrayList<>();
        String sql = "SELECT * FROM Carrera";
        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(new Carrera(rs.getInt("id_carrera"), rs.getString("nombre"), rs.getBoolean("activa")));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar carreras: " + e.getMessage());
        }
        return lista;
    }

    
    public boolean alternarEstadoCarrera(int idCarrera, boolean nuevoEstado) {
        String sql = "UPDATE Carrera SET activa = ? WHERE id_carrera = ?";
        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, idCarrera);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de carrera: " + e.getMessage());
            return false;
        }
    }
}