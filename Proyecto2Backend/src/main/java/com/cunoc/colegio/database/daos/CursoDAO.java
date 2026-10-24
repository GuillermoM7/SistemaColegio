package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.apirestapp.dto.CursoDTO;
import com.cunoc.colegio.apirestapp.dto.PaginaCursosDTO;
import com.cunoc.colegio.database.DBConnectionSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    
    public PaginaCursosDTO listarCursosPaginados(int pagina, int limite, String busqueda) {
        PaginaCursosDTO respuesta = new PaginaCursosDTO();
        List<CursoDTO> lista = new ArrayList<>();
        
        String filtro = (busqueda != null && !busqueda.trim().isEmpty()) ? "%" + busqueda + "%" : "%";
        
        String sqlCount = "SELECT COUNT(*) FROM curso WHERE nombre LIKE ?";
        String sqlData = "SELECT id_curso, nombre FROM curso WHERE nombre LIKE ? ORDER BY nombre ASC LIMIT ? OFFSET ?";

        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement psCount = conexion.prepareStatement(sqlCount)) {
             
            psCount.setString(1, filtro);
            try (ResultSet rsCount = psCount.executeQuery()) {
                int totalRegistros = 0;
                if (rsCount.next()) totalRegistros = rsCount.getInt(1);
                
                int totalPaginas = (int) Math.ceil((double) totalRegistros / limite);
                int offset = (pagina - 1) * limite;
                
                try (PreparedStatement psData = conexion.prepareStatement(sqlData)) {
                    psData.setString(1, filtro);
                    psData.setInt(2, limite);
                    psData.setInt(3, offset);
                    
                    try (ResultSet rsData = psData.executeQuery()) {
                        while (rsData.next()) {
                            CursoDTO dto = new CursoDTO();
                            dto.setIdCurso(rsData.getInt("id_curso"));
                            dto.setNombre(rsData.getString("nombre"));
                            lista.add(dto);
                        }
                    }
                }
                respuesta.setCursos(lista);
                respuesta.setTotalRegistros(totalRegistros);
                respuesta.setTotalPaginas(totalPaginas);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar cursos: " + e.getMessage());
        }
        return respuesta;
    }

    
    public boolean crearCurso(String nombre) throws SQLException {
        String sql = "INSERT INTO curso (nombre) VALUES (?)";
        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
             
            ps.setString(1, nombre.trim());
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new SQLException("El curso '" + nombre + "' ya existe en el sistema.");
            }
            throw e;
        }
    }
}