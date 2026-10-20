package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.apirestapp.dto.ListadoEmpleadoDTO;
import com.cunoc.colegio.apirestapp.dto.PaginaEmpleadosDTO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.cunoc.colegio.apirestapp.dto.RegistroEmpleadoDTO;
import com.cunoc.colegio.database.daos.EmpleadoDAO; 
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

@Path("/gestion-personal") 
public class GestionPersonal {
    
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    
    @POST
    @Path("/registrar-empleado") 
    @Consumes(MediaType.APPLICATION_JSON) 
    @Produces(MediaType.APPLICATION_JSON) 
    public Response registrarEmpleado(RegistroEmpleadoDTO dto) {
        

        boolean exito = empleadoDAO.registrarNuevoEmpleado(dto);
        

        if (exito) {
            
            String jsonExito = "{\"mensaje\": \"Empleado registrado correctamente en el sistema.\"}";
            return Response.status(Response.Status.CREATED).entity(jsonExito).build();
        } else {

            String jsonError = "{\"mensaje\": \"Error al registrar el empleado. Revisa los datos.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonError).build();
        }
    }
    
    
    @GET
    @Path("/listar") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarEmpleados(
            @DefaultValue("1") @QueryParam("pagina") int pagina,
            @DefaultValue("5") @QueryParam("limite") int limite) {
        
        try {
            PaginaEmpleadosDTO resultado = empleadoDAO.obtenerEmpleadosPaginados(pagina, limite);
            
            return Response.ok(resultado).build();
            
        } catch (Exception e) {
            e.printStackTrace();
            String jsonError = "{\"mensaje\": \"Error interno al obtener la lista de empleados.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonError).build();
        }
    }
    

    @PUT
    @Path("/cambiar-estado/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cambiarEstadoEmpleado(
            @PathParam("id") int idUsuario,
            @QueryParam("estado") boolean nuevoEstado) {
        
        try {
            
            if (!nuevoEstado) {
                if (empleadoDAO.esUltimoSuperAdminActivo(idUsuario)) {
                    String jsonError = "{\"mensaje\": \"Operación denegada: Debe existir al menos un Super Admin activo en el sistema.\"}";
                    return Response.status(Response.Status.BAD_REQUEST).entity(jsonError).build();
                }
            }
            
            boolean exito = empleadoDAO.cambiarEstadoUsuario(idUsuario, nuevoEstado);
            
            if (exito) {
                String jsonMensaje = "{\"mensaje\": \"Estado actualizado correctamente.\"}";
                return Response.ok(jsonMensaje).build();
            } else {
                String jsonError = "{\"mensaje\": \"No se encontró el usuario o no se pudo actualizar.\"}";
                return Response.status(Response.Status.NOT_FOUND).entity(jsonError).build();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            String jsonError = "{\"mensaje\": \"Error interno al cambiar el estado.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonError).build();
        }
    }
    
    
    @PUT
    @Path("/modificar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modificarEmpleado(ListadoEmpleadoDTO empleadoModificado) {
        try {
            
            if (empleadoModificado.getSalario() <= 0) {
                String jsonError = "{\"mensaje\": \"Operación rechazada: El salario debe ser mayor a 0.\"}";
                return Response.status(Response.Status.BAD_REQUEST).entity(jsonError).build();
            }

            boolean exito = empleadoDAO.modificarEmpleado(empleadoModificado);

            if (exito) {
                String jsonMensaje = "{\"mensaje\": \"Empleado actualizado correctamente.\"}";
                return Response.ok(jsonMensaje).build();
            } else {
                String jsonError = "{\"mensaje\": \"No se pudo actualizar el empleado en la base de datos.\"}";
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonError).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            String jsonError = "{\"mensaje\": \"Error interno al modificar el empleado.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonError).build();
        }
    }
}