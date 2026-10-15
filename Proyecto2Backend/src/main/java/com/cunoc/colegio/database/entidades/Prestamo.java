package com.cunoc.colegio.database.entidades;

import java.time.LocalDateTime;

public class Prestamo {
    
    private int idPrestamo;
    private int idLibro;
    private int idUsuario;
    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaEsperadaDevolucion;
    private LocalDateTime fechaDevolucionReal;

    public Prestamo() {
    }

    public Prestamo(int idPrestamo, int idLibro, int idUsuario, LocalDateTime fechaPrestamo, LocalDateTime fechaEsperadaDevolucion, LocalDateTime fechaDevolucionReal) {
        this.idPrestamo = idPrestamo;
        this.idLibro = idLibro;
        this.idUsuario = idUsuario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaEsperadaDevolucion = fechaEsperadaDevolucion;
        this.fechaDevolucionReal = fechaDevolucionReal;
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDateTime getFechaEsperadaDevolucion() {
        return fechaEsperadaDevolucion;
    }

    public void setFechaEsperadaDevolucion(LocalDateTime fechaEsperadaDevolucion) {
        this.fechaEsperadaDevolucion = fechaEsperadaDevolucion;
    }

    public LocalDateTime getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(LocalDateTime fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }
    
    
}
