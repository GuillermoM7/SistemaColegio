package com.cunoc.colegio.database.entidades;

public class Nota {
    
    private int idNota;
    private int idEstudiante;
    private int idAsignacion;
    private double zona;
    private double total;

    
    public Nota() {
    }

    
    public Nota(int idNota, int idEstudiante, int idAsignacion, double zona, double total) {
        this.idNota = idNota;
        this.idEstudiante = idEstudiante;
        this.idAsignacion = idAsignacion;
        this.zona = zona;
        this.total = total;
    }

    
    public int getIdNota() {
        return idNota;
    }

    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public double getZona() {
        return zona;
    }

    public void setZona(double zona) {
        this.zona = zona;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
        
}
