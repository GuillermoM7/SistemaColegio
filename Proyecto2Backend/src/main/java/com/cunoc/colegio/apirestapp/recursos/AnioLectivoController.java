package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.database.daos.AnioLectivoDAO;
import com.cunoc.colegio.database.entidades.AnioLectivo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;


@Path("/configuracion/anios")
public class AnioLectivoController {

    private AnioLectivoDAO anioDAO = new AnioLectivoDAO();


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    
    public Response listarAnios() {
        try {
            List<AnioLectivo> lista = anioDAO.listarAnios();
            return Response.ok(lista).build();
        } catch (Exception e) {
            String jsonError = "{\"mensaje\": \"Error al obtener los años lectivos.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonError).build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    
    public Response crearAnio(AnioLectivo nuevoAnio) {
        try {
            boolean exito = anioDAO.crearAnio(nuevoAnio);
            if (exito) {
                String jsonMensaje = "{\"mensaje\": \"Año lectivo registrado exitosamente.\"}";
                return Response.status(Response.Status.CREATED).entity(jsonMensaje).build();
            } else {
                String jsonError = "{\"mensaje\": \"No se pudo registrar el año lectivo. Verifique que no esté duplicado.\"}";
                return Response.status(Response.Status.BAD_REQUEST).entity(jsonError).build();
            }
        } catch (Exception e) {
            String jsonError = "{\"mensaje\": \"Error interno al crear el año lectivo.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonError).build();
        }
    }


    @PUT
    @Path("/activar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    
    public Response activarAnio(@PathParam("id") int idAnio) {
        try {
            boolean exito = anioDAO.activarAnioLectivo(idAnio);
            if (exito) {
                String jsonMensaje = "{\"mensaje\": \"Año lectivo activado correctamente. El sistema ahora opera bajo este ciclo.\"}";
                return Response.ok(jsonMensaje).build();
            } else {
                String jsonError = "{\"mensaje\": \"Error al intentar activar el año lectivo.\"}";
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonError).build();
            }
        } catch (Exception e) {
            String jsonError = "{\"mensaje\": \"Error interno en el servidor.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonError).build();
        }
    }
    
}