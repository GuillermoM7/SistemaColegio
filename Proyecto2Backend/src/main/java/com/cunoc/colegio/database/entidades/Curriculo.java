package com.cunoc.colegio.database.entidades;

public class Curriculo {
    
    private int idCurriculo;
    private int idGrado;
    private int idAnioLectivo;

        
    public Curriculo() {
    }

    public Curriculo(int idCurriculo, int idGrado, int idAnioLectivo) {
        this.idCurriculo = idCurriculo;
        this.idGrado = idGrado;
        this.idAnioLectivo = idAnioLectivo;
    }

    
    public int getIdCurriculo() {
        return idCurriculo;
    }

    public void setIdCurriculo(int idCurriculo) {
        this.idCurriculo = idCurriculo;
    }

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public int getIdAnioLectivo() {
        return idAnioLectivo;
    }

    public void setIdAnioLectivo(int idAnioLectivo) {
        this.idAnioLectivo = idAnioLectivo;
    }

     
}
