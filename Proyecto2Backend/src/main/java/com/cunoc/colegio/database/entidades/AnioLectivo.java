package com.cunoc.colegio.database.entidades;


public class AnioLectivo {
    
    private int idAnioLectivo;
    private int anio;
    private String fechaInicio; 
    private String fechaFin;
    private boolean activo;

    
    public AnioLectivo() {
    }

    
    public AnioLectivo(int idAnioLectivo, int anio, String fechaInicio, String fechaFin, boolean activo) {
        this.idAnioLectivo = idAnioLectivo;
        this.anio = anio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
    }

    
    public int getIdAnioLectivo() {
        return idAnioLectivo;
    }

    public void setIdAnioLectivo(int idAnioLectivo) {
        this.idAnioLectivo = idAnioLectivo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    
}
