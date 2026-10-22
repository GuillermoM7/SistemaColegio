package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.apirestapp.dto.RespuestaGradoDTO;
import com.cunoc.colegio.database.daos.CarreraDAO;
import com.cunoc.colegio.database.daos.GradoDAO;
import com.cunoc.colegio.database.daos.NivelDAO;
import com.cunoc.colegio.database.entidades.Carrera;
import com.cunoc.colegio.database.entidades.Grado;
import com.cunoc.colegio.database.entidades.Nivel;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;


@Path("/configuracion/academico")

public class AcademicoController {
    
    private NivelDAO nivelDAO = new NivelDAO();
    private GradoDAO gradoDAO = new GradoDAO();
    private CarreraDAO carreraDAO = new CarreraDAO();


    @GET
    @Path("/niveles")
    @Produces(MediaType.APPLICATION_JSON)
    
    public Response listarNiveles() {
        List<Nivel> lista = nivelDAO.listarNiveles();
        return Response.ok(lista).build();
    }


    @GET
    @Path("/grados/nivel/{idNivel}")
    @Produces(MediaType.APPLICATION_JSON)
    
    public Response listarGradosPorNivel(@PathParam("idNivel") int idNivel) {        
        List<RespuestaGradoDTO> lista = gradoDAO.listarGradosPorNivel(idNivel);
        return Response.ok(lista).build();
    }


    @GET
    @Path("/carreras")
    @Produces(MediaType.APPLICATION_JSON)
    
    public Response listarCarreras() {
        List<Carrera> lista = carreraDAO.listarCarreras();
        return Response.ok(lista).build();
    }


    @PUT
    @Path("/carreras/estado/{id}/{estado}")
    @Produces(MediaType.APPLICATION_JSON)
    
    public Response cambiarEstadoCarrera(@PathParam("id") int idCarrera, @PathParam("estado") boolean estado) {     
        boolean exito = carreraDAO.alternarEstadoCarrera(idCarrera, estado);
        if (exito) {
            return Response.ok("{\"mensaje\": \"Estado de la carrera actualizado.\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"mensaje\": \"Error al actualizar la carrera.\"}").build();
        }
    }
    
    
    @POST
    @Path("/crear-carreras")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    
    public Response crearCarrera(Carrera carrera) {      
        if (carreraDAO.crearCarrera(carrera)) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\": \"Carrera creada exitosamente.\"}")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"mensaje\": \"Error al crear la carrera. Verifique los datos en la BD.\"}")
                .build();
    }

    
    @POST
    @Path("/grados")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    
    public Response crearGrado(Grado grado) {
        if (gradoDAO.crearGrado(grado)) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\": \"Grado creado exitosamente.\"}")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"mensaje\": \"Error al crear el grado. Verifique los datos.\"}")
                .build();
    }
    
}