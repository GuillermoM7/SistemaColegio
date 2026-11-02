package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.database.DBConnectionSingleton;
import com.cunoc.colegio.database.entidades.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;


public class UsuarioDAO {

    
    public Usuario autenticar(String correo, String contrasena) {
        Usuario usuarioAutenticado = null;
        String sql = "SELECT * FROM usuario WHERE correo = ? AND contrasena = ? AND activo = 1";

        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String contrasenaCodificada = Base64.getEncoder().encodeToString(contrasena.getBytes());

            pstmt.setString(1, correo);
            pstmt.setString(2, contrasenaCodificada); 

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuarioAutenticado = new Usuario(rs.getInt("id_usuario"), rs.getString("correo"), rs.getString("contrasena"), rs.getInt("id_rol"), rs.getBoolean("activo"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
        }
        return usuarioAutenticado;
    }

    
    public boolean verificarCorreo(String correo) {
        String sql = "SELECT id_usuario FROM usuario WHERE correo = ? AND activo = 1";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar correo: " + e.getMessage());
        }
        return false;
    }

    
    public boolean actualizarContrasena(String correo, String nuevaContrasena) {
        String sql = "UPDATE usuario SET contrasena = ? WHERE correo = ? AND activo = 1";
        try (Connection conn = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String passCodificada = java.util.Base64.getEncoder().encodeToString(nuevaContrasena.getBytes());
            
            ps.setString(1, passCodificada);
            ps.setString(2, correo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
        }
        return false;
    }
    
}