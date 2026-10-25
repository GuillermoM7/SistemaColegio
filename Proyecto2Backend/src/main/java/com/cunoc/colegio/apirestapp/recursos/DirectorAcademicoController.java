package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.apirestapp.dto.CrearSeccionDTO;
import com.cunoc.colegio.database.daos.DirectorAcademicoDAO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;


@Path("/director/academico")
public class DirectorAcademicoController {

    private DirectorAcademicoDAO directorDAO = new DirectorAcademicoDAO();

    
    @GET
    @Path("/grados-curriculo/{idNivel}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarGradosCurriculo(
            @PathParam("idNivel") int idNivel,
            @QueryParam("idCarrera") Integer idCarrera) {
        
        return Response.ok(directorDAO.listarGradosConEstadoCurriculo(idNivel, idCarrera)).build();
    }
    
    
    @POST
    @Path("/curriculos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearCurriculo(com.cunoc.colegio.apirestapp.dto.CrearCurriculoDTO dto) {
        if (dto.getCursosIds() == null || dto.getCursosIds().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\": \"Debe asignar al menos un curso al currículo.\"}")
                    .build();
        }

        try {
            if (directorDAO.crearCurriculo(dto.getIdGrado(), dto.getCursosIds())) {
                return Response.status(Response.Status.CREATED)
                        .entity("{\"mensaje\": \"Currículo y cursos guardados exitosamente.\"}")
                        .build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\": \"" + e.getMessage() + "\"}")
                    .build();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"mensaje\": \"Error interno al crear el currículo.\"}")
                .build();
    }
    
    
    @GET
    @Path("/secciones/grado/{idGrado}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarSeccionesPorGrado(@PathParam("idGrado") int idGrado) {
        return Response.ok(directorDAO.listarSeccionesPorGrado(idGrado)).build();
    }
    
    
    @GET
    @Path("/curriculos/grados/{idGrado}/cursos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarCursosDeCurriculo(@PathParam("idGrado") int idGrado) {
        return Response.ok(directorDAO.listarCursosDeCurriculoActivo(idGrado)).build();
    }

    
    @GET
    @Path("/maestros")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarMaestros() {
        return Response.ok(directorDAO.listarMaestros()).build();
    }

    
    @POST
    @Path("/secciones")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearSeccion(CrearSeccionDTO dto) {
        try {
            if (directorDAO.crearSeccionConMaestros(dto)) {
                return Response.status(Response.Status.CREATED)
                        .entity("{\"mensaje\": \"Sección y maestros guardados exitosamente.\"}").build();
            }
        } catch (java.sql.SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\": \"" + e.getMessage() + "\"}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}