package com.cunoc.colegio.apirestapp.dto;

import java.util.List;

public class CrearCurriculoDTO {
    private int idGrado;
    private List<Integer> cursosIds;

    public CrearCurriculoDTO() {
    }

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public List<Integer> getCursosIds() {
        return cursosIds;
    }

    public void setCursosIds(List<Integer> cursosIds) {
        this.cursosIds = cursosIds;
    }
    
    
}
