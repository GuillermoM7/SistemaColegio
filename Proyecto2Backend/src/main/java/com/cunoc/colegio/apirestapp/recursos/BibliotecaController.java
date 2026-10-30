package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.apirestapp.dto.LibroDTO;
import com.cunoc.colegio.database.daos.BibliotecaDAO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/biblioteca")
public class BibliotecaController {

    private BibliotecaDAO dao = new BibliotecaDAO();

    
    @GET
    @Path("/libros")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerLibros() {
        return Response.ok(dao.listarLibros()).build();
    }

    
    @POST
    @Path("/libros")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarLibro(LibroDTO libro) {
        if (libro.getCantidadTotal() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"mensaje\":\"La cantidad total debe ser mayor a cero.\"}").build();
        }
        
        if (dao.crearLibro(libro)) {
            return Response.ok("{\"mensaje\":\"Libro registrado con éxito\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"mensaje\":\"Error al registrar\"}").build();
    }

    
    @PUT
    @Path("/libros/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarLibro(@PathParam("id") int id, LibroDTO libro) {
        if (libro.getCantidadTotal() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"mensaje\":\"La cantidad total debe ser mayor a cero.\"}").build();
        }

        libro.setIdLibro(id);
        if (dao.actualizarLibro(libro)) {
            return Response.ok("{\"mensaje\":\"Libro actualizado con éxito\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"mensaje\":\"Error al actualizar\"}").build();
    }
    
    
    @DELETE
    @Path("/libros/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarLibro(@PathParam("id") int id) {
        if (dao.eliminarLibro(id)) {
            return Response.ok("{\"mensaje\":\"Libro eliminado con éxito\"}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity("{\"mensaje\":\"Error al eliminar. Puede que el libro tenga préstamos activos.\"}")
                       .build();
    }
    
    
    @GET
    @Path("/usuarios/buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarUsuarios(@QueryParam("q") String query) {
        if (query == null || query.trim().isEmpty()) {
            return Response.ok(new ArrayList<>()).build();
        }
        return Response.ok(dao.buscarUsuariosParaPrestamo(query)).build();
    }

   
    @GET
    @Path("/libros/{id}/prestamos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarPrestamosActivos(@PathParam("id") int idLibro) {
        return Response.ok(dao.listarPrestamosActivosPorLibro(idLibro)).build();
    }


    @POST
    @Path("/libros/{id}/prestamos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response prestarLibro(@PathParam("id") int idLibro, java.util.Map<String, String> payload) {
        try {
            int idUsuario = Integer.parseInt(payload.get("idUsuario"));
            java.sql.Date fechaEsperada = java.sql.Date.valueOf(payload.get("fechaEsperada"));
            
            if (dao.registrarPrestamo(idLibro, idUsuario, fechaEsperada)) {
                return Response.ok("{\"mensaje\":\"Préstamo registrado exitosamente\"}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity("{\"mensaje\":\"Error al prestar. Es posible que ya no hayan copias disponibles.\"}").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"mensaje\":\"Datos de préstamo inválidos\"}").build();
        }
    }
    
    
    @GET
    @Path("/usuarios/{idUsuario}/prestamos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPrestamosDeUsuario(@PathParam("idUsuario") int idUsuario) {
        return Response.ok(dao.listarPrestamosPorUsuario(idUsuario)).build();
    }

    
    @POST
    @Path("/devoluciones")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response procesarDevolucion(java.util.Map<String, Double> payload) {
        try {
            int idPrestamo = payload.get("idPrestamo").intValue();
            int idLibro = payload.get("idLibro").intValue();
            double montoMulta = payload.get("montoMulta");
            
            if (dao.registrarDevolucion(idPrestamo, idLibro, montoMulta)) {
                return Response.ok("{\"mensaje\":\"Devolución exitosa\"}").build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    
    @GET
    @Path("/usuarios/{idUsuario}/multas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerMultasDeUsuario(@PathParam("idUsuario") int idUsuario) {
        return Response.ok(dao.listarMultasPorUsuario(idUsuario)).build();
    }

    
    @GET
    @Path("/reportes/mora")
    @Produces(MediaType.APPLICATION_JSON)
    public Response generarDataReporteMora() {
        return Response.ok(dao.obtenerReportePrestamosEnMora()).build();
    }
    
}