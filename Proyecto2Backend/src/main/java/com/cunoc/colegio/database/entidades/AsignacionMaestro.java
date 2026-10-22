package com.cunoc.colegio.database.entidades;

public class AsignacionMaestro {
    
    private int idAsignacion;
    private int idEmpleado;
    private int idCurso;
    private int idSeccion;
    private int idAnioLectivo;

    
    public AsignacionMaestro() {
    }

    public AsignacionMaestro(int idAsignacion, int idEmpleado, int idCurso, int idSeccion, int idAnioLectivo) {
        this.idAsignacion = idAsignacion;
        this.idEmpleado = idEmpleado;
        this.idCurso = idCurso;
        this.idSeccion = idSeccion;
        this.idAnioLectivo = idAnioLectivo;
    }

    
    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public int getIdAnioLectivo() {
        return idAnioLectivo;
    }

    public void setIdAnioLectivo(int idAnioLectivo) {
        this.idAnioLectivo = idAnioLectivo;
    }

      
}
