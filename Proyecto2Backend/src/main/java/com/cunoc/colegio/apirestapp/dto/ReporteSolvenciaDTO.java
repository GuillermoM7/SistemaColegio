package com.cunoc.colegio.apirestapp.dto;

import java.math.BigDecimal;

public class ReporteSolvenciaDTO {
    private int idEstudiante;
    private String nombreCompleto;
    private BigDecimal totalCargos;
    private BigDecimal totalPagado;
    private BigDecimal saldoPendiente;
    private String estado;

    
    public ReporteSolvenciaDTO() {
    }

    
    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public BigDecimal getTotalCargos() {
        return totalCargos;
    }

    public void setTotalCargos(BigDecimal totalCargos) {
        this.totalCargos = totalCargos;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public BigDecimal getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(BigDecimal saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
}