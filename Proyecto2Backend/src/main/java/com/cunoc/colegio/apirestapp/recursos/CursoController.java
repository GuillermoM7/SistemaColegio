package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.apirestapp.dto.CursoDTO;
import com.cunoc.colegio.database.daos.CursoDAO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/director/cursos")
public class CursoController {

    private CursoDAO cursoDAO = new CursoDAO();

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarPaginados(@QueryParam("pagina") @DefaultValue("1") int pagina,
                                    @QueryParam("limite") @DefaultValue("5") int limite,
                                    @QueryParam("busqueda") @DefaultValue("") String busqueda) {
        return Response.ok(cursoDAO.listarCursosPaginados(pagina, limite, busqueda)).build();
    }

    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearCurso(CursoDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\": \"El nombre del curso es obligatorio.\"}")
                    .build();
        }

        try {
            if (cursoDAO.crearCurso(dto.getNombre())) {
                return Response.status(Response.Status.CREATED)
                        .entity("{\"mensaje\": \"Curso creado exitosamente.\"}")
                        .build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"mensaje\": \"" + e.getMessage() + "\"}")
                    .build();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"mensaje\": \"Error interno al crear el curso.\"}")
                .build();
    }
    
}