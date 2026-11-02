package com.cunoc.colegio.apirestapp.dto;

public class HistorialPagoDTO {
    private String tipo;
    private String concepto;
    private Double montoAdeudado;
    private Double montoPagado;
    private String fechaPago;
    private String estado;

    public HistorialPagoDTO() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Double getMontoAdeudado() {
        return montoAdeudado;
    }

    public void setMontoAdeudado(Double montoAdeudado) {
        this.montoAdeudado = montoAdeudado;
    }

    public Double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(Double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
}