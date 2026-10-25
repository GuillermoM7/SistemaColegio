package com.cunoc.colegio.apirestapp.dto;

import java.util.List;


public class CrearSeccionDTO {
    private int idGrado;
    private String nombreSeccion;
    private List<AsignacionDTO> asignaciones;

    
    public CrearSeccionDTO() {
    }

    
    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public String getNombreSeccion() {
        return nombreSeccion;
    }

    public void setNombreSeccion(String nombreSeccion) {
        this.nombreSeccion = nombreSeccion;
    }

    public List<AsignacionDTO> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<AsignacionDTO> asignaciones) {
        this.asignaciones = asignaciones;
    }
    
    
}
