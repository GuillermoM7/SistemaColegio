package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.apirestapp.dto.ListadoEmpleadoDTO;
import com.cunoc.colegio.apirestapp.dto.RegistroEmpleadoDTO;
import com.cunoc.colegio.database.daos.EmpleadoDAO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/director/personal")
public class DirectorPersonalController {

    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarPersonal(@QueryParam("pagina") @DefaultValue("1") int pagina,
                                   @QueryParam("limite") @DefaultValue("5") int limite) {
        return Response.ok(empleadoDAO.obtenerPersonalOperativoPaginado(pagina, limite)).build();
    }

    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarPersonal(RegistroEmpleadoDTO dto) {
        if (dto.getIdRol() == 1 || dto.getIdRol() == 2) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"mensaje\": \"Permiso denegado: No puede crear perfiles administrativos.\"}")
                    .build();
        }

        if (empleadoDAO.registrarNuevoEmpleado(dto)) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\": \"Personal registrado exitosamente.\"}")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"mensaje\": \"Error al registrar al empleado. Verifique los datos.\"}")
                .build();
    }

    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modificarPersonal(ListadoEmpleadoDTO dto) {
        if (empleadoDAO.modificarPersonalOperativo(dto)) {
            return Response.ok("{\"mensaje\": \"Datos del empleado actualizados exitosamente.\"}")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"mensaje\": \"Error al actualizar los datos.\"}")
                .build();
    }
}