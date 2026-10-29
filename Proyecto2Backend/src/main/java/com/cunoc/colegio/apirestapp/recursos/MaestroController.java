package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.apirestapp.dto.ActividadDTO;
import com.cunoc.colegio.apirestapp.dto.AsistenciaDTO;
import com.cunoc.colegio.apirestapp.dto.NotaActividadDTO;
import com.cunoc.colegio.apirestapp.dto.NotaExamenDTO;
import com.cunoc.colegio.database.daos.MaestroDAO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/maestro")
public class MaestroController {

    private MaestroDAO dao = new MaestroDAO();

    @GET
    @Path("/usuarios/{idUsuario}/cursos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarMisCursos(@PathParam("idUsuario") int idUsuario) {
        return Response.ok(dao.listarCursosAsignados(idUsuario)).build();
    }
    
    
    @GET
    @Path("/asignaciones/{idAsignacion}/alumnos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarNominaClase(@PathParam("idAsignacion") int idAsignacion) {
        return Response.ok(dao.listarNominaAlumnos(idAsignacion)).build();
    }
    
    
    @POST
    @Path("/asignaciones/{idAsignacion}/asistencias")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarAsistencia(@PathParam("idAsignacion") int idAsignacion, List<AsistenciaDTO> listaAsistencia) {
        
        if (listaAsistencia == null || listaAsistencia.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"mensaje\":\"La lista está vacía\"}").build();
        }

        boolean exito = dao.registrarAsistenciaMasiva(listaAsistencia);
        
        if (exito) {
            return Response.ok("{\"mensaje\":\"Asistencia guardada correctamente\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"mensaje\":\"Error al guardar asistencia\"}").build();
        }
    }
    
    
    @POST
    @Path("/asignaciones/{idAsignacion}/actividades")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearActividad(@PathParam("idAsignacion") int idAsignacion, ActividadDTO actividad) {
        actividad.setIdAsignacion(idAsignacion);
        if (dao.crearActividad(actividad)) {
            return Response.ok("{\"mensaje\":\"Actividad creada\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    
    @GET
    @Path("/asignaciones/{idAsignacion}/actividades")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarActividades(@PathParam("idAsignacion") int idAsignacion) {
        return Response.ok(dao.listarActividades(idAsignacion)).build();
    }

    
    @POST
    @Path("/asignaciones/{idAsignacion}/calificaciones")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response calificarActividad(@PathParam("idAsignacion") int idAsignacion, List<NotaActividadDTO> notas) {
        if (dao.calificarActividadMasiva(idAsignacion, notas)) {
            return Response.ok("{\"mensaje\":\"Calificaciones guardadas y zona actualizada\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    
    
    @GET
    @Path("/actividades/{idActividad}/calificaciones")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerNotasActividad(@PathParam("idActividad") int idActividad) {
        return Response.ok(dao.obtenerNotasPorActividad(idActividad)).build();
    }
    
    
    @GET
    @Path("/asignaciones/{idAsignacion}/examen-final")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerNotasExamen(@PathParam("idAsignacion") int idAsignacion) {
        return Response.ok(dao.obtenerNotasExamenFinal(idAsignacion)).build();
    }

    
    @POST
    @Path("/asignaciones/{idAsignacion}/examen-final")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response calificarExamen(@PathParam("idAsignacion") int idAsignacion, List<NotaExamenDTO> notas) {
        if (dao.calificarExamenFinalMasivo(idAsignacion, notas)) {
            return Response.ok("{\"mensaje\":\"Notas de examen final guardadas exitosamente\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    
}