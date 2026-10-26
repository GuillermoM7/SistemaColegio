package com.cunoc.colegio.apirestapp.dto;


public class ExpedienteAcademicoDTO {
    private int anioLectivo;
    private String nombreCurso;
    private Double zona;
    private Double examenFinal;
    private Double notaTotal;

    public ExpedienteAcademicoDTO() {
    }

    public int getAnioLectivo() {
        return anioLectivo;
    }

    public void setAnioLectivo(int anioLectivo) {
        this.anioLectivo = anioLectivo;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
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
