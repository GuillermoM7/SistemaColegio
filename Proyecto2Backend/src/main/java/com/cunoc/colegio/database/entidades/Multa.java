package com.cunoc.colegio.database.entidades;

public class Multa {
    
    private int idMulta;
    private int idPrestamo;
    private double monto;
    private EstadoMulta estado;

    
    public Multa() {
    }

    
    public Multa(int idMulta, int idPrestamo, double monto, EstadoMulta estado) {
        this.idMulta = idMulta;
        this.idPrestamo = idPrestamo;
        this.monto = monto;
        this.estado = estado;
    }

    
    public int getIdMulta() {
        return idMulta;
    }

    public void setIdMulta(int idMulta) {
        this.idMulta = idMulta;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public EstadoMulta getEstado() {
        return estado;
    }

    public void setEstado(EstadoMulta estado) {
        this.estado = estado;
    }
    
    
}
