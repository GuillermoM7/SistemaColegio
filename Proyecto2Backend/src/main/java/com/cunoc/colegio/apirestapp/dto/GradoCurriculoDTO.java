package com.cunoc.colegio.apirestapp.dto;

public class GradoCurriculoDTO {
    private int idGrado;
    private String nombreGrado;
    private String nombreCarrera;
    private boolean tieneCurriculoActivo;

    
    public GradoCurriculoDTO() {
    }

    
    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public String getNombreGrado() {
        return nombreGrado;
    }

    public void setNombreGrado(String nombreGrado) {
        this.nombreGrado = nombreGrado;
    }

    public String getNombreCarrera() {
        return nombreCarrera;
    }

    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }

    public boolean isTieneCurriculoActivo() {
        return tieneCurriculoActivo;
    }

    public void setTieneCurriculoActivo(boolean tieneCurriculoActivo) {
        this.tieneCurriculoActivo = tieneCurriculoActivo;
    }
    
    
}
