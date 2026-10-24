package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.apirestapp.dto.RegistroUsuarioAlumnoDTO;
import com.cunoc.colegio.database.daos.EstudianteDAO;
import com.cunoc.colegio.database.daos.EmpleadoDAO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/director/alumnos")
public class DirectorAlumnoController {

    private EstudianteDAO estudianteDAO = new EstudianteDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO(); 

    
    @GET
    @Path("/sin-usuario")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarHuerfanos() {
        return Response.ok(estudianteDAO.obtenerAlumnosSinUsuario()).build();
    }

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarPaginados(@QueryParam("pagina") @DefaultValue("1") int pagina,
                                    @QueryParam("limite") @DefaultValue("5") int limite) {
        return Response.ok(estudianteDAO.obtenerAlumnosConUsuarioPaginado(pagina, limite)).build();
    }

    
    @POST
    @Path("/crear-usuario")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearUsuario(RegistroUsuarioAlumnoDTO dto) {
        int ID_ROL_ALUMNO = 5; 
        
        if (estudianteDAO.enlazarUsuarioAEstudiante(dto.getIdEstudiante(), dto.getCorreo(), dto.getPassword(), ID_ROL_ALUMNO)) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\": \"Usuario asignado correctamente al estudiante.\"}")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"mensaje\": \"Error al asignar usuario. Verifique los datos.\"}")
                .build();
    }

    
    @PUT
    @Path("/cambiar-estado/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cambiarEstado(@PathParam("id") int idUsuario, @QueryParam("estado") boolean nuevoEstado) {
        if (empleadoDAO.cambiarEstadoUsuario(idUsuario, nuevoEstado)) {
            return Response.ok("{\"mensaje\": \"Estado actualizado correctamente.\"}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"mensaje\": \"Error al actualizar el estado.\"}")
                .build();
    }
}