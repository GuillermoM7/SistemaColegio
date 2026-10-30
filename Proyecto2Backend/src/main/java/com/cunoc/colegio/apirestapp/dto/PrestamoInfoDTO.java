package com.cunoc.colegio.apirestapp.dto;


public class PrestamoInfoDTO {
    private int idPrestamo;
    private int idUsuario;
    private String correo;
    private String nombreCompleto;
    private String fechaPrestamo;
    private String fechaEsperada;

    
    public PrestamoInfoDTO() {
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
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

      
}