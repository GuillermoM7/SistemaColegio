package com.cunoc.colegio.database.entidades;

public class Grado {
    
    private int idGrado;
    private String nombre;
    private int nivel;
    private int carrera;

    
    public Grado() {
    }

    
    public Grado(int idgrado, String nombre, int nivel, int carrera) {
        this.idGrado = idgrado;
        this.nombre = nombre;
        this.nivel = nivel;
        this.carrera = carrera;
    }

    
    public int getIdgrado() {
        return idGrado;
    }

    public void setIdgrado(int idgrado) {
        this.idGrado = idgrado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getCarrera() {
        return carrera;
    }

    public void setCarrera(int carrera) {
        this.carrera = carrera;
    }
    
    
}
