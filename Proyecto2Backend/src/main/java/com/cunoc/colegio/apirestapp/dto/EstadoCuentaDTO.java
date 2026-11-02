package com.cunoc.colegio.apirestapp.dto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EstadoCuentaDTO {
    private String nombreEstudiante;
    private BigDecimal saldoPendiente;
    private List<HistorialPagoDTO> pagos = new ArrayList<>(); 

    public EstadoCuentaDTO() {
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public BigDecimal getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(BigDecimal saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    public List<HistorialPagoDTO> getPagos() {
        return pagos;
    }

    public void setPagos(List<HistorialPagoDTO> pagos) {
        this.pagos = pagos;
    }

    
}