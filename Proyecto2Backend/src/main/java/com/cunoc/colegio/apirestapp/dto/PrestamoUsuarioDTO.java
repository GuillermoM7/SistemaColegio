package com.cunoc.colegio.apirestapp.dto;

public class PrestamoUsuarioDTO {
    private int idPrestamo;
    private int idLibro;
    private String tituloLibro;
    private String fechaPrestamo;
    private String fechaEsperada;
    private int diasAtraso;
    private double montoMulta;

    
    public PrestamoUsuarioDTO() {
    }

    
    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public String getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(String fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getFechaEsperada() {
        return fechaEsperada;
    }

    public void setFechaEsperada(String fechaEsperada) {
        this.fechaEsperada = fechaEsperada;
    }

    public int getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(int diasAtraso) {
        this.diasAtraso = diasAtraso;
    }

    public double getMontoMulta() {
        return montoMulta;
    }

    public void setMontoMulta(double montoMulta) {
        this.montoMulta = montoMulta;
    }


}