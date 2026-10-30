package com.cunoc.colegio.apirestapp.dto;

public class LibroDTO {
    private int idLibro;
    private String titulo;
    private String autor;
    private String editorial;
    private int cantidadDisponible;
    private int CantidadTotal;

    
    public LibroDTO() {}

    
    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public int getCantidadTotal() {
        return CantidadTotal;
    }

    public void setCantidadTotal(int CantidadTotal) {
        this.CantidadTotal = CantidadTotal;
    }
   
}