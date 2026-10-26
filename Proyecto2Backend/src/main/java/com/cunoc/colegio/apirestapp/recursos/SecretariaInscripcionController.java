package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.apirestapp.dto.InscripcionDTO;
import com.cunoc.colegio.database.daos.SecretariaInscripcionDAO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/secretaria/inscripciones")
public class SecretariaInscripcionController {

    private SecretariaInscripcionDAO dao = new SecretariaInscripcionDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response inscribirEstudiante(InscripcionDTO dto) {
        try {
            String mensaje = dao.procesarInscripcion(dto);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\": \"" + mensaje + "\"}").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\": \"" + e.getMessage() + "\"}").build();
        }
    }
}