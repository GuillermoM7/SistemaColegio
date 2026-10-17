package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.database.DBConnectionSingleton;
import com.cunoc.colegio.database.entidades.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    
    public Usuario autenticar(String correo, String contrasena) {
        Usuario usuarioAutenticado = null;
        
        String sql = "SELECT * FROM usuario WHERE correo = ? AND contrasena = ? AND activo = 1";


        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, correo);
            pstmt.setString(2, contrasena);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuarioAutenticado = new Usuario(rs.getInt("id_usuario"),rs.getString("correo"),rs.getString("contrasena"),rs.getInt("id_rol"),rs.getBoolean("activo"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
        }

        return usuarioAutenticado;
    }
}