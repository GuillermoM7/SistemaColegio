package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.apirestapp.dto.EstadoCuentaDTO;
import com.cunoc.colegio.apirestapp.dto.ReporteMoraDTO;
import com.cunoc.colegio.apirestapp.dto.ReporteSolvenciaDTO;
import com.cunoc.colegio.apirestapp.dto.ResumenIngresosDTO;
import com.cunoc.colegio.database.daos.BibliotecaDAO;
import com.cunoc.colegio.database.daos.ReportesDAO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Path("/reportes")
public class ReportesController {

    @GET
    @Path("/solvencia")
    @Produces("application/pdf")
    public Response reporteSolvencia(@QueryParam("idGrado") int idGrado, @QueryParam("idSeccion") int idSeccion) {
        try {
            // Obtener la lista de datos procesados desde el DAO
            ReportesDAO dao = new ReportesDAO();
            List<ReporteSolvenciaDTO> listaDatos = dao.obtenerAlumnosSolventesYMorosos(idGrado, idSeccion);

            //Convertir la lista a un formato compatible con JasperReports
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listaDatos);

            // Cargar el diseño del reporte
            InputStream reporteStream = getClass().getResourceAsStream("/reportes/reporteSolvencia.jrxml");
            if (reporteStream == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("{\"mensaje\": \"No se encontró el archivo de diseño del reporte.\"}")
                               .type("application/json")
                               .build();
            }

            //Compilar y llenar el reporte con los datos
            JasperReport jasperReport = JasperCompileManager.compileReport(reporteStream);
            Map<String, Object> parametrosExtra = new HashMap<>(); // Para enviar títulos o fechas si lo necesitas
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametrosExtra, dataSource);

            // Exportar el resultado a PDF 
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            // Retornar el PDF al frontend
            return Response.ok(pdfBytes)
                    .header("Content-Disposition", "inline; filename=\"Reporte_Solvencia.pdf\"")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\": \"Error al generar el reporte: " + e.getMessage() + "\"}")
                    .type("application/json")
                    .build();
        }
    }
    
    
    @GET
    @Path("/historial-pagos")
    @Produces("application/pdf")
    public Response reporteHistorialPagos(
            @QueryParam("idEstudiante") int idEstudiante, 
            @QueryParam("fechaInicio") String fechaInicio, 
            @QueryParam("fechaFin") String fechaFin) {
        
        try {
            boolean sinFiltro = (fechaInicio == null || fechaInicio.trim().isEmpty() || fechaFin == null || fechaFin.trim().isEmpty());
            
            if (sinFiltro) {
                fechaInicio = "2000-01-01";
                fechaFin = "2100-12-31";
            }
            ReportesDAO dao = new ReportesDAO();
            EstadoCuentaDTO datosReporte = dao.obtenerHistorialPagosEstudiante(idEstudiante, fechaInicio, fechaFin);
            System.out.println("Pagos encontrados para el PDF: " + datosReporte.getPagos().size());

            //Preparar el DataSource exclusivo para las filas de la tabla
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datosReporte.getPagos());

            //Preparar los Parámetros para el encabezado
            Map<String, Object> parametros = new HashMap<>();
            
            // Validamos nulos en caso de que el estudiante no exista o no tenga cargos
            String nombre = datosReporte.getNombreEstudiante() != null ? datosReporte.getNombreEstudiante() : "Estudiante no encontrado";
            BigDecimal saldo = datosReporte.getSaldoPendiente() != null ? datosReporte.getSaldoPendiente() : BigDecimal.ZERO;
            String textoPeriodo = sinFiltro ? "Historial Completo" : fechaInicio + " al " + fechaFin;
            parametros.put("nombreEstudiante", nombre);
            parametros.put("rangoFechas", textoPeriodo);
            parametros.put("saldoPendiente", saldo);

            InputStream reporteStream = getClass().getResourceAsStream("/reportes/historialPagos.jrxml");
            if (reporteStream == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"No se encontró el archivo de diseño del reporte.\"}")
                        .type("application/json")
                        .build();
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reporteStream);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            return Response.ok(pdfBytes)
                    .header("Content-Disposition", "inline; filename=\"Historial_Pagos_" + idEstudiante + ".pdf\"")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\": \"Error al generar el reporte: " + e.getMessage() + "\"}")
                    .type("application/json")
                    .build();
        }
    }
    
    
    @GET
    @Path("/ingresos")
    @Produces("application/pdf")
    public Response reporteIngresos(
            @QueryParam("fechaInicio") String fechaInicio, 
            @QueryParam("fechaFin") String fechaFin) {
        
        try {
            boolean sinFiltro = (fechaInicio == null || fechaInicio.trim().isEmpty() || fechaFin == null || fechaFin.trim().isEmpty());
            
            if (sinFiltro) {
                fechaInicio = "2000-01-01";
                fechaFin = "2100-12-31";
            }
            
            ReportesDAO dao = new ReportesDAO();
            ResumenIngresosDTO datosReporte = dao.obtenerReporteIngresos(fechaInicio, fechaFin);

            // DataSource para la tabla
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datosReporte.getListaIngresos());

            // Parámetros para el encabezado
            Map<String, Object> parametros = new HashMap<>();
            String textoPeriodo = sinFiltro ? "Historial Completo" : fechaInicio + " al " + fechaFin;
            BigDecimal total = datosReporte.getTotalIngresos() != null ? datosReporte.getTotalIngresos() : BigDecimal.ZERO;
            
            parametros.put("rangoFechas", textoPeriodo);
            parametros.put("totalIngresos", total);

            // Carga y compilación
            InputStream reporteStream = getClass().getResourceAsStream("/reportes/reporteIngresos.jrxml");
            if (reporteStream == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"No se encontró el archivo de diseño del reporte.\"}")
                        .type("application/json")
                        .build();
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reporteStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            return Response.ok(pdfBytes)
                    .header("Content-Disposition", "inline; filename=\"Reporte_Ingresos.pdf\"")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\": \"Error al generar el reporte: " + e.getMessage() + "\"}")
                    .type("application/json")
                    .build();
        }
    }
    
    
    @GET
    @Path("/mora-biblioteca")
    @Produces("application/pdf")
    public Response reporteMoraBiblioteca() {
        try {
            BibliotecaDAO dao = new BibliotecaDAO();
            List<ReporteMoraDTO> listaMora = dao.obtenerReportePrestamosEnMora();

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listaMora);
            Map<String, Object> parametros = new HashMap<>();

            InputStream reporteStream = getClass().getResourceAsStream("/reportes/reporteMora.jrxml");
            if (reporteStream == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"No se encontró el archivo de diseño del reporte.\"}")
                        .type("application/json")
                        .build();
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reporteStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            return Response.ok(pdfBytes)
                    .header("Content-Disposition", "inline; filename=\"Reporte_Mora_Biblioteca.pdf\"")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\": \"Error al generar reporte de mora: " + e.getMessage() + "\"}")
                    .type("application/json")
                    .build();
        }
    }
}