package com.cunoc.colegio.apirestapp.dto;


public class InscripcionDTO {
    private int idEstudiante;
    private int idSeccion;

    
    public InscripcionDTO() {
    }

    
    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }
    
    
}
