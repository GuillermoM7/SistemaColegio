package com.cunoc.colegio.database.entidades;

import java.time.LocalDate;

public class Inscripcion {
    
    private int idInscripcion;
    private int idEstudiante;
    private int idGrado;
    private int idSeccion;
    private LocalDate fechaInscripcion;
    private int idAnioLectivo;

    
    public Inscripcion() {
    }

    
    public Inscripcion(int idInscripcion, int idEstudiante, int idGrado, int idSeccion, LocalDate fechaInscripcion, int idAnioLectivo) {
        this.idInscripcion = idInscripcion;
        this.idEstudiante = idEstudiante;
        this.idGrado = idGrado;
        this.idSeccion = idSeccion;
        this.fechaInscripcion = fechaInscripcion;
        this.idAnioLectivo = idAnioLectivo;
    }

    
    public int getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public int getIdAnioLectivo() {
        return idAnioLectivo;
    }

    public void setIdAnioLectivo(int idAnioLectivo) {
        this.idAnioLectivo = idAnioLectivo;
    }

    
}
