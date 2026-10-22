package com.cunoc.colegio.apirestapp.dto;


public class RespuestaGradoDTO {
    
    private int idGrado;
    private String nombre;
    private Integer idNivel;
    private Integer idCarrera;
    private String nombreCarrera;

    public RespuestaGradoDTO() {
    }

    public RespuestaGradoDTO(int idGrado, String nombre, Integer idNivel, Integer idCarrera, String nombreCarrera) {
        this.idGrado = idGrado;
        this.nombre = nombre;
        this.idNivel = idNivel;
        this.idCarrera = idCarrera;
        this.nombreCarrera = nombreCarrera;
    }

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(Integer idNivel) {
        this.idNivel = idNivel;
    }

    public Integer getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Integer idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getNombreCarrera() {
        return nombreCarrera;
    }

    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }
    
    
}
