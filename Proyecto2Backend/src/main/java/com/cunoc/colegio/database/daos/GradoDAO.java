package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.apirestapp.dto.RespuestaGradoDTO;
import com.cunoc.colegio.database.DBConnectionSingleton;
import com.cunoc.colegio.database.entidades.Grado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GradoDAO {
    
    
    public boolean crearGrado(Grado grado) {
        String sql = "INSERT INTO Grado (nombre, id_nivel, id_carrera) VALUES (?, ?, ?)";
        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
             
            ps.setString(1, grado.getNombre());
            ps.setInt(2, grado.getIdNivel());
            

            if (grado.getIdCarrera() != null && grado.getIdCarrera() > 0) {
                ps.setInt(3, grado.getIdCarrera());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear grado: " + e.getMessage());
            return false;
        }
    }
    
    
// OBTENER GRADOS POR NIVEL
    public List<RespuestaGradoDTO> listarGradosPorNivel(int idNivel) {
        List<RespuestaGradoDTO> lista = new ArrayList<>();
        
        String sql = "SELECT g.*, c.nombre AS nombre_carrera FROM Grado g " +
                     "LEFT JOIN Carrera c ON g.id_carrera = c.id_carrera " +
                     "WHERE g.id_nivel = ?";
                     
        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
             
            ps.setInt(1, idNivel);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RespuestaGradoDTO grado = new RespuestaGradoDTO();
                    grado.setIdGrado(rs.getInt("id_grado"));
                    grado.setNombre(rs.getString("nombre"));
                    grado.setIdNivel(rs.getInt("id_nivel"));
                    
                    int idCarrera = rs.getInt("id_carrera");
                    if (!rs.wasNull()) {
                        grado.setIdCarrera(idCarrera);
                        grado.setNombreCarrera(rs.getString("nombre_carrera")); 
                    }
                    lista.add(grado);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar grados: " + e.getMessage());
        }
        return lista;
    }

}