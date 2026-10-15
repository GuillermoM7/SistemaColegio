package com.cunoc.colegio.database.entidades;

public class Carrera {
    
    private int idCarrera;
    private String nombre;
    private boolean estado;

    
    public Carrera() {
    }

    
    public Carrera(int idCarrera, String nombre, boolean estado) {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
        this.estado = estado;
    }

    
    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
      
}
