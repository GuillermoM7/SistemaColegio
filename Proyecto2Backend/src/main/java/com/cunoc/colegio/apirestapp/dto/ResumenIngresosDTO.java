package com.cunoc.colegio.apirestapp.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ResumenIngresosDTO {
    private BigDecimal totalIngresos;
    private List<IngresoDTO> listaIngresos = new ArrayList<>(); 

    public ResumenIngresosDTO() {
    }

    public BigDecimal getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(BigDecimal totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public List<IngresoDTO> getListaIngresos() {
        return listaIngresos;
    }

    public void setListaIngresos(List<IngresoDTO> listaIngresos) {
        this.listaIngresos = listaIngresos;
    }

    
}