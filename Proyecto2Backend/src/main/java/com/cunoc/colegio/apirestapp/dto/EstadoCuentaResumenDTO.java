package com.cunoc.colegio.apirestapp.dto;
import java.util.List;

public class EstadoCuentaResumenDTO {
    private double saldoTotal;
    private List<MovimientoFinancieroDTO> movimientos;

    
    public EstadoCuentaResumenDTO() {
    }

    
    public double getSaldoTotal() {
        return saldoTotal;
    }

    public void setSaldoTotal(double saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public List<MovimientoFinancieroDTO> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoFinancieroDTO> movimientos) {
        this.movimientos = movimientos;
    }

    
    
}