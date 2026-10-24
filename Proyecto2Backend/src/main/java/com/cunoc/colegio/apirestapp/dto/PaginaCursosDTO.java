package com.cunoc.colegio.apirestapp.dto;

import java.util.List;

public class PaginaCursosDTO {
    private List<CursoDTO> cursos;
    private int totalRegistros;
    private int totalPaginas;

    public PaginaCursosDTO() {
    }

    public List<CursoDTO> getCursos() {
        return cursos;
    }

    public void setCursos(List<CursoDTO> cursos) {
        this.cursos = cursos;
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
