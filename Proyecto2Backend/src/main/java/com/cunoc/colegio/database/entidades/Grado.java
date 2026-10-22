package com.cunoc.colegio.database.entidades;

public class Grado {
    
    private int idGrado;
    private String nombre;
    private Integer idNivel;
    private Integer idCarrera;

    
    public Grado() {
    }

    
    public Grado(int idGrado, String nombre, Integer idNivel, Integer idCarrera) {
        this.idGrado = idGrado;
        this.nombre = nombre;
        this.idNivel = idNivel;
        this.idCarrera = idCarrera;
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

    
}
