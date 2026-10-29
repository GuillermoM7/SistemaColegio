package com.cunoc.colegio.apirestapp.dto;

public class NotaExamenDTO {
    private int idEstudiante;
    private double notaExamen;

    public NotaExamenDTO() {
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public double getNotaExamen() {
        return notaExamen;
    }

    public void setNotaExamen(double notaExamen) {
        this.notaExamen = notaExamen;
    }
    
}