package com.cunoc.colegio.apirestapp.dto;

public class NotaActividadDTO {
    private int idEstudiante;
    private int idActividad;
    private double punteoObtenido;

    public NotaActividadDTO() {
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public double getPunteoObtenido() {
        return punteoObtenido;
    }

    public void setPunteoObtenido(double punteoObtenido) {
        this.punteoObtenido = punteoObtenido;
    }

    
}