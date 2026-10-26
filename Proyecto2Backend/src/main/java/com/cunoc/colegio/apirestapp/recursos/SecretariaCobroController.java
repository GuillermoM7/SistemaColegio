package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.apirestapp.dto.RegistrarPagoDTO;
import com.cunoc.colegio.database.daos.SecretariaCobroDAO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/secretaria/cobros")
public class SecretariaCobroController {

    private SecretariaCobroDAO dao = new SecretariaCobroDAO();

    @GET
    @Path("/estudiantes/{idEstudiante}/pendientes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarCargosPendientes(@PathParam("idEstudiante") int idEstudiante) {
        return Response.ok(dao.listarCargosPendientes(idEstudiante)).build();
    }

    @POST
    @Path("/pagar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response procesarPago(RegistrarPagoDTO dto) {
        try {
            if (dao.procesarPagoCargo(dto)) {
                return Response.ok("{\"mensaje\": \"Pago procesado exitosamente.\"}").build();
            }
        } catch (java.sql.SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\": \"" + e.getMessage() + "\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    
    @GET
    @Path("/estudiantes/{idEstudiante}/historial")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarHistorialPagos(@PathParam("idEstudiante") int idEstudiante) {
        return Response.ok(dao.listarHistorialPagos(idEstudiante)).build();
    }
    
    
    @GET
    @Path("/{idPago}/recibo")
    @Produces("application/pdf")
    public Response descargarRecibo(@PathParam("idPago") int idPago) {
        byte[] pdf = dao.generarReciboPDF(idPago);
        if (pdf != null) {
            return Response.ok(pdf)
                    .header("Content-Disposition", "inline; filename=Recibo_" + idPago + ".pdf")
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Recibo no encontrado").build();
    }
    
    
    @GET
    @Path("/estudiantes/{idEstudiante}/solvencia")
    @Produces("application/pdf")
    public Response descargarSolvencia(@PathParam("idEstudiante") int idEstudiante) {
        byte[] pdf = dao.generarSolvenciaPDF(idEstudiante);
        if (pdf != null) {
            return Response.ok(pdf)
                    .header("Content-Disposition", "inline; filename=Solvencia_" + idEstudiante + ".pdf")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("No se pudo generar la solvencia. Verifique que no existan deudas.").build();
    }
}