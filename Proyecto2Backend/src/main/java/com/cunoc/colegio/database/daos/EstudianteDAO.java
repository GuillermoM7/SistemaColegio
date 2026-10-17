package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.database.DBConnectionSingleton;
import com.cunoc.colegio.database.entidades.Estado;
import com.cunoc.colegio.database.entidades.Estudiante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstudianteDAO {

    public Estudiante obtenerEstudiantePorIdUsuario(int idUsuario) {
        
        Estudiante estudiante = null;
        
        String query = "SELECT * FROM estudiante WHERE id_usuario = ?";

        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idUsuario);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    estudiante = new Estudiante();
                    estudiante.setIdEstudiante(rs.getInt("id_estudiante"));
                    estudiante.setIdUsuario(rs.getInt("id_usuario"));
                    estudiante.setNombres(rs.getString("nombres"));
                    estudiante.setApellidos(rs.getString("apellidos"));
                    estudiante.setInfoMedica(rs.getString("info_medica"));
                    estudiante.setDatosEncargados(rs.getString("datos_encargados"));
                    String estadoDesdeDB = rs.getString("estado");
                    
                    if(estadoDesdeDB != null){
                        String estadoNormalizado = estadoDesdeDB.toUpperCase();
                        estudiante.setEstado(Estado.valueOf(estadoNormalizado));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el estudiante: " + e.getMessage());
        }
        
        return estudiante;
    }
}