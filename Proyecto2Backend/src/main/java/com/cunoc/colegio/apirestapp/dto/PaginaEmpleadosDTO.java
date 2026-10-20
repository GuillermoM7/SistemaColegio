
package com.cunoc.colegio.apirestapp.dto;

import java.util.List;

public class PaginaEmpleadosDTO {
    private List<ListadoEmpleadoDTO> empleados;
    private int totalRegistros;
    private int totalPaginas;

    public PaginaEmpleadosDTO() {
    }

    
    public List<ListadoEmpleadoDTO> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<ListadoEmpleadoDTO> empleados) {
        this.empleados = empleados;
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
