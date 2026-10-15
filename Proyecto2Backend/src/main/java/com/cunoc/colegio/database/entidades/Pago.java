package com.cunoc.colegio.database.entidades;

import java.time.LocalDateTime;

public class Pago {
    
    private int idPago;
    private int idEstudiante;
    private Concepto concepto;
    private double monto;
    private LocalDateTime fechaPago;
    private int idSecretaria;

    
    public Pago() {
    }

    
    public Pago(int idPago, int idEstudiante, Concepto concepto, double monto, LocalDateTime fechaPago, int idSecretaria) {
        this.idPago = idPago;
        this.idEstudiante = idEstudiante;
        this.concepto = concepto;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.idSecretaria = idSecretaria;
    }

    
    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getIdSecretaria() {
        return idSecretaria;
    }

    public void setIdSecretaria(int idSecretaria) {
        this.idSecretaria = idSecretaria;
    }
    
    
}
