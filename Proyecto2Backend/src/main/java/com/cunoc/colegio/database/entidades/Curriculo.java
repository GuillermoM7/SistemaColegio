package com.cunoc.colegio.database.entidades;

public class Curriculo {
    
    private int idCurriculo;
    private int idGrado;
    private int cicloEscolar;

        
    public Curriculo() {
    }

    
    public Curriculo(int idCurriculo, int idGrado, int cicloEscolar) {
        this.idCurriculo = idCurriculo;
        this.idGrado = idGrado;
        this.cicloEscolar = cicloEscolar;
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

    public int getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(int cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }
        
        
}
