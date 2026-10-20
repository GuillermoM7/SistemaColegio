package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.apirestapp.dto.ListadoEmpleadoDTO;
import com.cunoc.colegio.apirestapp.dto.PaginaEmpleadosDTO;
import com.cunoc.colegio.apirestapp.dto.RegistroEmpleadoDTO;
import com.cunoc.colegio.database.DBConnectionSingleton;
import com.cunoc.colegio.database.entidades.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
    
     
    public boolean registrarNuevoEmpleado(RegistroEmpleadoDTO dto) {
        Connection conexion = null;
        PreparedStatement psUsuario = null;
        PreparedStatement psEmpleado = null;
        ResultSet rsKeys = null;

        try {
            conexion = DBConnectionSingleton.getInstance().getConnection(); 
            conexion.setAutoCommit(false); 

            String sqlUsuario = "INSERT INTO usuario (correo, contrasena, id_rol) VALUES (?, ?, ?)";
            psUsuario = conexion.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
            psUsuario.setString(1, dto.getCorreo());       
            psUsuario.setString(2, dto.getPassword());           
            psUsuario.setInt(3, dto.getIdRol());    
            psUsuario.executeUpdate();

            rsKeys = psUsuario.getGeneratedKeys();
            int idGenerado = 0;
            if (rsKeys.next()) {
                idGenerado = rsKeys.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el ID del usuario.");
            }


            String sqlEmpleado = "INSERT INTO empleado (id_usuario, nombres, apellidos, puesto, fecha_contratacion, salario) VALUES (?, ?, ?, ?, ?, ?)";
            psEmpleado = conexion.prepareStatement(sqlEmpleado);
            psEmpleado.setInt(1, idGenerado);
            psEmpleado.setString(2, dto.getNombres());
            psEmpleado.setString(3, dto.getApellidos());
            psEmpleado.setString(4, dto.getPuesto());
            psEmpleado.setDate(5, java.sql.Date.valueOf(dto.getFechaContratacion()));
            psEmpleado.setDouble(6, dto.getSalario());
        
            psEmpleado.executeUpdate();

            conexion.commit();
            return true;

            
        } catch (Exception e) {
            if (conexion != null) {
                try {
                    conexion.rollback();
                    System.out.println("Se ejecutó Rollback por error en la transacción.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try { 
                if (rsKeys != null) rsKeys.close(); 
            } catch (SQLException e) { }
            try { 
                if (psEmpleado != null) psEmpleado.close(); 
            } catch (SQLException e) { }
            try { 
                if (psUsuario != null) psUsuario.close(); 
            } catch (SQLException e) { }
            try { 
                if (conexion != null) {
                    conexion.setAutoCommit(true);
                    conexion.close(); 
                } 
            } catch (SQLException e) { }
        }
    }
    

    public PaginaEmpleadosDTO obtenerEmpleadosPaginados(int pagina, int limite) {
        PaginaEmpleadosDTO respuesta = new PaginaEmpleadosDTO();
        List<ListadoEmpleadoDTO> lista = new ArrayList<>();
    
        Connection conexion = null;
        PreparedStatement psCount = null;
        PreparedStatement psData = null;
        ResultSet rsCount = null;
        ResultSet rsData = null;

        try {
            conexion = DBConnectionSingleton.getInstance().getConnection(); 
        
            String sqlCount = "SELECT COUNT(*) FROM usuario u INNER JOIN empleado e ON u.id_usuario = e.id_usuario WHERE u.id_rol IN (1, 2)";
            psCount = conexion.prepareStatement(sqlCount);
            rsCount = psCount.executeQuery();
        
            int totalRegistros = 0;
            if (rsCount.next()) {
                totalRegistros = rsCount.getInt(1);
            }
        
            int totalPaginas = (int) Math.ceil((double) totalRegistros / limite);
        
            int offset = (pagina - 1) * limite;
            String sqlData = 
                "SELECT u.id_usuario, u.correo, u.id_rol, u.activo, " +
                "e.id_empleado, e.nombres, e.apellidos, e.puesto, e.fecha_contratacion, e.salario " +
                "FROM usuario u INNER JOIN empleado e ON u.id_usuario = e.id_usuario " +
                "WHERE u.id_rol IN (1, 2) " +
                "ORDER BY u.id_usuario DESC LIMIT ? OFFSET ?";
                         
            psData = conexion.prepareStatement(sqlData);
            psData.setInt(1, limite); 
            psData.setInt(2, offset);
            rsData = psData.executeQuery();

            while (rsData.next()) {
                ListadoEmpleadoDTO emp = new ListadoEmpleadoDTO();
            
                //Usuario
                emp.setIdUsuario(rsData.getInt("id_usuario"));
                emp.setCorreo(rsData.getString("correo"));
                emp.setIdRol(rsData.getInt("id_rol"));
                emp.setEstado(rsData.getBoolean("activo")); 
            
                //Empleado
                emp.setIdEmpleado(rsData.getInt("id_empleado"));
                emp.setNombre(rsData.getString("nombres"));
                emp.setApellidos(rsData.getString("apellidos"));
                emp.setPuesto(rsData.getString("puesto"));
                emp.setSalario(rsData.getDouble("salario"));
            
                java.sql.Date fechaDb = rsData.getDate("fecha_contratacion");
                if (fechaDb != null) {
                    emp.setFechaContratacion(fechaDb.toString()); 
                } else {
                    emp.setFechaContratacion("");
                }
            
                lista.add(emp);
            }
        
            respuesta.setEmpleados(lista);
            respuesta.setTotalRegistros(totalRegistros);
            respuesta.setTotalPaginas(totalPaginas);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsData != null) rsData.close(); 
            } catch (Exception e) {}
            try { 
                if (psData != null) psData.close(); 
            } catch (Exception e) {}
            try { 
                if (rsCount != null) rsCount.close(); 
            } catch (Exception e) {}
            try {
                if (psCount != null) psCount.close(); 
            } catch (Exception e) {}
            try { 
                if (conexion != null) conexion.close(); 
            } catch (Exception e) {}
        }
    
        return respuesta;
    }
    
    
    public boolean cambiarEstadoUsuario(int idUsuario, boolean nuevoEstado) {
        Connection conexion = null;
        PreparedStatement ps = null;
        
        try {
            conexion = DBConnectionSingleton.getInstance().getConnection();
            
            String sql = "UPDATE usuario SET activo = ? WHERE id_usuario = ?";
            
            ps = conexion.prepareStatement(sql);
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, idUsuario);
            
            int filasAfectadas = ps.executeUpdate();
            
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado del usuario: " + e.getMessage());
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            try { if (conexion != null) conexion.close(); } catch (SQLException e) {}
        }
    }
    

    public boolean esUltimoSuperAdminActivo(int idUsuario) {
        String sqlCheckRol = "SELECT id_rol FROM usuario WHERE id_usuario = ?";
        String sqlCount = "SELECT COUNT(*) FROM usuario WHERE id_rol = 1 AND activo = true";
        
        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection()) {
            
            try (PreparedStatement psRol = conexion.prepareStatement(sqlCheckRol)) {
                psRol.setInt(1, idUsuario);
                try (ResultSet rsRol = psRol.executeQuery()) {
                    if (rsRol.next()) {
                        if (rsRol.getInt("id_rol") != 1) {
                            return false; 
                        }
                    } else {
                        return false; 
                    }
                }
            }
            
            try (PreparedStatement psCount = conexion.prepareStatement(sqlCount);
                 ResultSet rsCount = psCount.executeQuery()) {
                if (rsCount.next()) {
                    int totalActivos = rsCount.getInt(1);
                    return totalActivos <= 1; 
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar último super admin: " + e.getMessage());
            return true; 
        }
        return false;
    }
    

    public boolean modificarEmpleado(ListadoEmpleadoDTO dto) {
        Connection conexion = null;
        PreparedStatement psEmpleado = null;
        PreparedStatement psUsuario = null;

        try {
            conexion = DBConnectionSingleton.getInstance().getConnection();
            conexion.setAutoCommit(false);

            String sqlEmpleado = "UPDATE empleado SET nombres = ?, apellidos = ?, puesto = ?, salario = ? WHERE id_empleado = ?";
            psEmpleado = conexion.prepareStatement(sqlEmpleado);
            psEmpleado.setString(1, dto.getNombre());
            psEmpleado.setString(2, dto.getApellidos());
            psEmpleado.setString(3, dto.getPuesto());
            psEmpleado.setDouble(4, dto.getSalario());
            psEmpleado.setInt(5, dto.getIdEmpleado());
            psEmpleado.executeUpdate();

            String sqlUsuario = "UPDATE usuario SET id_rol = ? WHERE id_usuario = ?";
            psUsuario = conexion.prepareStatement(sqlUsuario);
            psUsuario.setInt(1, dto.getIdRol());
            psUsuario.setInt(2, dto.getIdUsuario());
            psUsuario.executeUpdate();

            conexion.commit(); 
            return true;

        } catch (SQLException e) {
            if (conexion != null) {
                try { conexion.rollback(); } catch (SQLException ex) { }
            }
            System.err.println("Error al modificar empleado: " + e.getMessage());
            return false;
        } finally {
            try { 
                if (psEmpleado != null) psEmpleado.close(); 
            } catch (SQLException e) { }
            try { 
                if (psUsuario != null) psUsuario.close(); 
            } catch (SQLException e) { }
            try { 
                if (conexion != null) {
                    conexion.setAutoCommit(true); conexion.close(); 
                } 
            } catch (SQLException e) { }
        }
    }
}