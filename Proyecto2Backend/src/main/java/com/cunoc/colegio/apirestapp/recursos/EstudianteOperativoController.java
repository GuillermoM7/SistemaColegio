package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.apirestapp.dto.EstadoCuentaResumenDTO;
import com.cunoc.colegio.apirestapp.dto.PerfilEstudianteDTO;
import com.cunoc.colegio.database.daos.EstudianteOperativoDAO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/estudiante/operativo")
public class EstudianteOperativoController {

    private EstudianteOperativoDAO dao = new EstudianteOperativoDAO();

    @GET
    @Path("/perfil/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPerfil(@PathParam("idUsuario") int idUsuario) {
        PerfilEstudianteDTO perfil = dao.consultarPerfilCompleto(idUsuario);
        if (perfil != null) {
            return Response.ok(perfil).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("{\"mensaje\": \"Perfil no encontrado o sin inscripciones activas.\"}").build();
    }
    
    
    @GET
    @Path("/estado-cuenta/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerEstadoCuenta(@PathParam("idUsuario") int idUsuario) {
        EstadoCuentaResumenDTO estadoCuenta = dao.consultarEstadoCuenta(idUsuario);
        return Response.ok(estadoCuenta).build();
    }
    
}