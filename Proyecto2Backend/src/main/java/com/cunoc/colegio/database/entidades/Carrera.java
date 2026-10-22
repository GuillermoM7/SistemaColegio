package com.cunoc.colegio.database.entidades;

public class Carrera {
    
    private int idCarrera;
    private String nombre;
    private boolean activa;

    
    public Carrera() {
    }

    
    public Carrera(int idCarrera, String nombre, boolean activa) {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
        this.activa = activa;
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

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

        
}
