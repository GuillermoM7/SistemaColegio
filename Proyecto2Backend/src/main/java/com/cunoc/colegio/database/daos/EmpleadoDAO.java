package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.database.DBConnectionSingleton;
import com.cunoc.colegio.database.entidades.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpleadoDAO {

    public Empleado obtenerEmpleadoPorIdUsuario(int idUsuario) {
        Empleado empleado = null;
        String query = "SELECT * FROM empleado WHERE id_usuario = ?";

        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idUsuario);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    empleado = new Empleado();
                    empleado.setIdEmpleado(rs.getInt("id_empleado"));
                    empleado.setIdUsuario(rs.getInt("id_usuario"));
                    empleado.setNombres(rs.getString("nombres"));
                    empleado.setApellidos(rs.getString("apellidos"));
                    empleado.setPuesto(rs.getString("puesto"));
                    java.sql.Date fechaSql = rs.getDate("fecha_contratacion");
                    empleado.setSalario(rs.getInt("salario"));

                    if(fechaSql != null){
                        empleado.setFechaContratacion(fechaSql.toLocalDate());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el estudiante: " + e.getMessage());
        }
        
        return empleado;
    }
}