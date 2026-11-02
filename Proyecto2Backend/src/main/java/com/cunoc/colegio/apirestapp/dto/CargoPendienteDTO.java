package com.cunoc.colegio.apirestapp.dto;

public class CargoPendienteDTO {
    private int idCargo;
    private String concepto;
    private Double montoCobrado;
    private Integer mesReferencia;

    
    public CargoPendienteDTO() {
    }

    
    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Double getMontoCobrado() {
        return montoCobrado;
    }

    public void setMontoCobrado(Double montoCobrado) {
        this.montoCobrado = montoCobrado;
    }

    public Integer getMesReferencia() {
        return mesReferencia;
    }

    public void setMesReferencia(Integer mesReferencia) {
        this.mesReferencia = mesReferencia;
    }

    
}