package com.cunoc.colegio.apirestapp.dto;

import java.util.List;

public class PaginaAlumnosDTO {
    private List<ListadoAlumnoDTO> alumnos;
    private int totalRegistros;
    private int totalPaginas;

    public PaginaAlumnosDTO() {
    }

    
    public List<ListadoAlumnoDTO> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<ListadoAlumnoDTO> alumnos) {
        this.alumnos = alumnos;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(int totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = totalPaginas;
    }
    
    
}
