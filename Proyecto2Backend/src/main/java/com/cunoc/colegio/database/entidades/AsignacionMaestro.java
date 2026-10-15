package com.cunoc.colegio.database.entidades;

public class AsignacionMaestro {
    
    private int idAsignacion;
    private int idEmpleado;
    private int idCurso;
    private int idSeccion;
    private int cicloEscolar;

    
    public AsignacionMaestro() {
    }

    
    public AsignacionMaestro(int idAsignacion, int idEmpleado, int idCurso, int idSeccion, int cicloEscolar) {
        this.idAsignacion = idAsignacion;
        this.idEmpleado = idEmpleado;
        this.idCurso = idCurso;
        this.idSeccion = idSeccion;
        this.cicloEscolar = cicloEscolar;
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

    public int getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(int cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }
    
      
}
