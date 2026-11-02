package com.cunoc.colegio.database.daos;

import com.cunoc.colegio.apirestapp.dto.EstadoCuentaDTO;
import com.cunoc.colegio.apirestapp.dto.HistorialPagoDTO;
import com.cunoc.colegio.apirestapp.dto.IngresoDTO;
import com.cunoc.colegio.apirestapp.dto.ReporteSolvenciaDTO;
import com.cunoc.colegio.apirestapp.dto.ResumenIngresosDTO;
import com.cunoc.colegio.database.DBConnectionSingleton;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReportesDAO {

    public List<ReporteSolvenciaDTO> obtenerAlumnosSolventesYMorosos(int idGrado, int idSeccion) {
        List<ReporteSolvenciaDTO> lista = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

            String sql = "SELECT e.id_estudiante, CONCAT(e.nombres, ' ', e.apellidos) AS nombre_completo, " +
                     // Extrae la suma de cargos y usa COALESCE para convertir valores NULL en 0 si no hay deudas
                     "COALESCE(cargos.total_cargos, 0) AS total_cargos, " +
                     // Extrae la suma de pagos y convierte valores NULL en 0 si el alumno no ha pagado nada
                     "COALESCE(pagos.total_pagado, 0) AS total_pagado, " +
                     // Calcula la mora matemática en tiempo real restando lo pagado de la deuda total
                     "(COALESCE(cargos.total_cargos, 0) - COALESCE(pagos.total_pagado, 0)) AS saldo_pendiente " +
                     "FROM estudiante e " +
                     "JOIN inscripcion i ON e.id_estudiante = i.id_estudiante " +
                     "JOIN seccion s ON i.id_seccion = s.id_seccion " +
                     "JOIN curriculo cu ON s.id_curriculo = cu.id_curriculo " +
                     // Subconsulta 1: Agrupa y suma todos los registros en la tabla 'cargo' por alumno
                     "LEFT JOIN (SELECT id_estudiante, SUM(monto_cobrado) as total_cargos FROM cargo GROUP BY id_estudiante) cargos " +
                     "ON e.id_estudiante = cargos.id_estudiante " +
                     // Subconsulta 2: Agrupa y suma todos los recibos cancelados en la tabla 'pago' por alumno
                     "LEFT JOIN (SELECT id_estudiante, SUM(monto) as total_pagado FROM pago GROUP BY id_estudiante) pagos " +
                     "ON e.id_estudiante = pagos.id_estudiante " +
                     "WHERE cu.id_grado = ? AND s.id_seccion = ?";

        try {
            conexion = DBConnectionSingleton.getInstance().getConnection();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idGrado);
            ps.setInt(2, idSeccion);
            rs = ps.executeQuery();

            while (rs.next()) {
                ReporteSolvenciaDTO dto = new ReporteSolvenciaDTO();
                dto.setIdEstudiante(rs.getInt("id_estudiante"));
                dto.setNombreCompleto(rs.getString("nombre_completo"));
                
                BigDecimal cargos = rs.getBigDecimal("total_cargos");
                BigDecimal pagado = rs.getBigDecimal("total_pagado");
                BigDecimal saldo = rs.getBigDecimal("saldo_pendiente");
                
                dto.setTotalCargos(cargos);
                dto.setTotalPagado(pagado);
                dto.setSaldoPendiente(saldo);
                
                if (saldo.compareTo(BigDecimal.ZERO) > 0) {
                    dto.setEstado("Moroso");
                } else {
                    dto.setEstado("Solvente");
                }
                
                lista.add(dto);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error en el reporte de solvencia: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { 
                if (rs != null) rs.close(); 
            } catch (Exception e) {}
            
            try { 
                if (ps != null) ps.close(); 
            } catch (Exception e) {}
            
            try { 
                if (conexion != null) conexion.close(); 
            } catch (Exception e) {}
        }
        return lista;
    }
    
    
    public EstadoCuentaDTO obtenerHistorialPagosEstudiante(int idEstudiante, String fechaInicio, String fechaFin) {
        EstadoCuentaDTO estadoCuenta = new EstadoCuentaDTO();
        List<HistorialPagoDTO> listaPagos = new ArrayList<>();
        double saldoTotal = 0.0;
        
        Connection conexion = null;
        PreparedStatement psNombre = null;
        PreparedStatement psMovimientos = null;
        ResultSet rsNombre = null;
        ResultSet rsMovimientos = null;

        String sqlNombre = "SELECT CONCAT(nombres, ' ', apellidos) AS nombre_completo FROM estudiante WHERE id_estudiante = ?";

        String sqlMovimientos = 
                // bloque1: CARGOS ACADÉMICOS Y EXTRACURRICULARES
                "(SELECT 'CARGO ACADÉMICO' AS tipo, c.concepto, c.monto_cobrado AS monto_adeudado, " +
                "p.monto AS monto_pagado, p.fecha_pago, c.estado_pago AS estado " +
                "FROM cargo c " +
                // Se usa LEFT JOIN para incluir los cargos que aún NO tienen un pago registrado en la tabla 'pago'
                "LEFT JOIN pago p ON c.id_cargo = p.id_cargo " +
                // Filtra por estudiante y aplica el rango de fechas SOLO si existe una fecha_pago, sino lo incluye igual
                "WHERE c.id_estudiante = ? AND (p.fecha_pago IS NULL OR DATE(p.fecha_pago) BETWEEN ? AND ?)) " +                
                // UNION ALL junta los resultados del Bloque 1 y el Bloque 2 en una sola lista unificada
                "UNION ALL " +
                
                // Bloque2: MULTAS DE BIBLIOTECA
                "(SELECT 'MULTA BIBLIOTECA' AS tipo, 'Atraso en devolución' AS concepto, m.monto AS monto_adeudado, " +
                "p.monto AS monto_pagado, p.fecha_pago, m.estado AS estado " +
                "FROM multa m " +
                "JOIN prestamo pr ON m.id_prestamo = pr.id_prestamo " +
                "JOIN estudiante e ON pr.id_usuario = e.id_usuario " +
                // Se usa LEFT JOIN para incluir multas que aún NO han sido pagadas
                "LEFT JOIN pago p ON m.id_multa = p.id_multa " +
                // Filtra por estudiante y aplica el mismo rango de fechas
                "WHERE e.id_estudiante = ? AND (p.fecha_pago IS NULL OR DATE(p.fecha_pago) BETWEEN ? AND ?)) " +              
                // Ordena la lista final unificada: primero los pendientes (deudas actuales), luego por fecha de pago más reciente
                "ORDER BY estado DESC, fecha_pago DESC";

        try {
            conexion = DBConnectionSingleton.getInstance().getConnection();
            //Obtener Nombre del Estudiante
            psNombre = conexion.prepareStatement(sqlNombre);
            psNombre.setInt(1, idEstudiante);
            rsNombre = psNombre.executeQuery();
            if (rsNombre.next()) {
                estadoCuenta.setNombreEstudiante(rsNombre.getString("nombre_completo"));
            }
            //Obtener Movimientos Financieros
            psMovimientos = conexion.prepareStatement(sqlMovimientos);
            psMovimientos.setInt(1, idEstudiante);
            psMovimientos.setString(2, fechaInicio);
            psMovimientos.setString(3, fechaFin);
            psMovimientos.setInt(4, idEstudiante);
            psMovimientos.setString(5, fechaInicio);
            psMovimientos.setString(6, fechaFin);
            rsMovimientos = psMovimientos.executeQuery();

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");

            while (rsMovimientos.next()) {
                HistorialPagoDTO mov = new HistorialPagoDTO();
                mov.setTipo(rsMovimientos.getString("tipo"));
                mov.setConcepto(rsMovimientos.getString("concepto"));
                
                double adeudado = rsMovimientos.getDouble("monto_adeudado");
                mov.setMontoAdeudado(adeudado);
                
                Object pagado = rsMovimientos.getObject("monto_pagado");
                mov.setMontoPagado(pagado != null ? ((Number) pagado).doubleValue() : 0.0);
                
                java.sql.Timestamp fechaP = rsMovimientos.getTimestamp("fecha_pago");
                mov.setFechaPago(fechaP != null ? sdf.format(fechaP) : "N/A");
                
                String estado = rsMovimientos.getString("estado");
                mov.setEstado(estado);
                
                if ("PENDIENTE".equalsIgnoreCase(estado)) {
                    saldoTotal += adeudado;
                }
                
                listaPagos.add(mov);
            }
            
            estadoCuenta.setPagos(listaPagos);
            estadoCuenta.setSaldoPendiente(BigDecimal.valueOf(saldoTotal));

        } catch (java.sql.SQLException e) {
            System.err.println("Error en el historial de pagos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rsMovimientos != null) rsMovimientos.close(); } catch (Exception e) {}
            try { if (rsNombre != null) rsNombre.close(); } catch (Exception e) {}
            try { if (psMovimientos != null) psMovimientos.close(); } catch (Exception e) {}
            try { if (psNombre != null) psNombre.close(); } catch (Exception e) {}
            try { if (conexion != null) conexion.close(); } catch (Exception e) {}
        }
        return estadoCuenta;
    }
   
    
    public ResumenIngresosDTO obtenerReporteIngresos(String fechaInicio, String fechaFin) {
        ResumenIngresosDTO resumen = new ResumenIngresosDTO();
        List<IngresoDTO> lista = new ArrayList<>();
        double totalSumado = 0.0;
        
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = 
                //Extraemos el número de recibo, la fecha y concatenamos el nombre completo del alumno
                "SELECT p.id_pago AS recibo, p.fecha_pago AS fecha, " +
                "CONCAT(e.nombres, ' ', e.apellidos) AS alumno, " +
                //Traemos el concepto (Colegiatura, Inscripción, Actividad, Multa) y la cantidad de dinero ingresada
                "p.concepto, p.monto " +
                "FROM pago p " +
                //Cruzamos con la tabla estudiante basándonos en el ID para saber a quién le pertenece el recibo
                "JOIN estudiante e ON p.id_estudiante = e.id_estudiante " +
                //Convertimos el timestamp a DATE puro para que el rango de búsqueda (BETWEEN) sea exacto por días
                "WHERE DATE(p.fecha_pago) BETWEEN ? AND ? " +
                //Ordenamos los recibos cronológicamente, de los más antiguos a los más recientes del rango
                "ORDER BY p.fecha_pago ASC";

        try {
            conexion = DBConnectionSingleton.getInstance().getConnection();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            
            rs = ps.executeQuery();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");

            while (rs.next()) {
                IngresoDTO ingreso = new IngresoDTO();
                ingreso.setRecibo(rs.getInt("recibo"));
                ingreso.setAlumno(rs.getString("alumno"));
                ingreso.setConcepto(rs.getString("concepto"));
                
                double monto = rs.getDouble("monto");
                ingreso.setMonto(monto);
                
                java.sql.Timestamp fechaP = rs.getTimestamp("fecha");
                ingreso.setFecha(fechaP != null ? sdf.format(fechaP) : "N/A");
                
                totalSumado += monto;
                lista.add(ingreso);
            }
            
            resumen.setListaIngresos(lista);
            resumen.setTotalIngresos(BigDecimal.valueOf(totalSumado));

        } catch (java.sql.SQLException e) {
            System.err.println("Error SQL en Reporte de Ingresos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conexion != null) conexion.close(); } catch (Exception e) {}
        }
        
        return resumen;
    }
    
}