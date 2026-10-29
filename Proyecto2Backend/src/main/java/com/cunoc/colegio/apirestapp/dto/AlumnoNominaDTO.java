package com.cunoc.colegio.apirestapp.dto;

public class AlumnoNominaDTO {
    private int idEstudiante;
    private String nombres;
    private String apellidos;
    
    private Double zona;
    private Double examenFinal;
    private Double notaTotal;
    
    public AlumnoNominaDTO() {
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Double getZona() {
        return zona;
    }

    public void setZona(Double zona) {
        this.zona = zona;
    }

    public Double getExamenFinal() {
        return examenFinal;
    }

    public void setExamenFinal(Double examenFinal) {
        this.examenFinal = examenFinal;
    }

    public Double getNotaTotal() {
        return notaTotal;
    }

    public void setNotaTotal(Double notaTotal) {
        this.notaTotal = notaTotal;
    }

}