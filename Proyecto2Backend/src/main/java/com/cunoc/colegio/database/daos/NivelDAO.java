package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.database.DBConnectionSingleton;
import com.cunoc.colegio.database.entidades.Nivel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NivelDAO {
    
    
    public List<Nivel> listarNiveles() {
        List<Nivel> lista = new ArrayList<>();
        String sql = "SELECT * FROM Nivel ORDER BY id_nivel ASC";
        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Nivel(rs.getInt("id_nivel"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar niveles: " + e.getMessage());
        }
        return lista;
    }
}