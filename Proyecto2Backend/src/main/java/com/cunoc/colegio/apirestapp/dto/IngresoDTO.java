package com.cunoc.colegio.apirestapp.dto;

public class IngresoDTO {
    private int recibo;
    private String fecha;
    private String alumno;
    private String concepto;
    private Double monto;

    
    public IngresoDTO() {
    }

    
    public int getRecibo() {
        return recibo;
    }

    public void setRecibo(int recibo) {
        this.recibo = recibo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAlumno() {
        return alumno;
    }

    public void setAlumno(String alumno) {
        this.alumno = alumno;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
    
}