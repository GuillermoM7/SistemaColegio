package com.cunoc.colegio.database.entidades;

public class CurriculoCurso {
    
    private int idCurriculo;
    private int idCurso;

    
    public CurriculoCurso() {
    }

    
    public CurriculoCurso(int idCurriculo, int idCurso) {
        this.idCurriculo = idCurriculo;
        this.idCurso = idCurso;
    }

    
    public int getIdCurriculo() {
        return idCurriculo;
    }

    public void setIdCurriculo(int idCurriculo) {
        this.idCurriculo = idCurriculo;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }
    
    
}
