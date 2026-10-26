package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.apirestapp.dto.EstudianteDTO;
import com.cunoc.colegio.database.daos.SecretariaEstudianteDAO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/secretaria/estudiantes")
public class SecretariaEstudianteController {

    private SecretariaEstudianteDAO dao = new SecretariaEstudianteDAO();

    
    @GET
    @Path("/buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarEstudiantes(@QueryParam("q") String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            return Response.ok(new java.util.ArrayList<>()).build();
        }
        List<EstudianteDTO> resultados = dao.buscarEstudiantes(filtro);
        return Response.ok(resultados).build();
    }

    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearEstudiante(EstudianteDTO dto) {
        if (dao.crearEstudiante(dto)) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\": \"Estudiante creado exitosamente.\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"mensaje\": \"Error al crear el estudiante.\"}").build();
    }

    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarEstudiante(@PathParam("id") int id, EstudianteDTO dto) {
        dto.setIdEstudiante(id);
        if (dao.actualizarEstudiante(dto)) {
            return Response.ok("{\"mensaje\": \"Información actualizada exitosamente.\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"mensaje\": \"Error al actualizar la información.\"}").build();
    }

    
    @PUT
    @Path("/{id}/estado")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cambiarEstado(@PathParam("id") int id, EstudianteDTO dto) {
        if (dao.cambiarEstado(id, dto.getEstado())) {
            return Response.ok("{\"mensaje\": \"Estado actualizado a " + dto.getEstado() + ".\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"mensaje\": \"Error al actualizar el estado.\"}").build();
    }
    
    
    @GET
    @Path("/{id}/expediente")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerExpediente(@PathParam("id") int idEstudiante) {
        return Response.ok(dao.consultarExpedienteAcademico(idEstudiante)).build();
    }
    
}