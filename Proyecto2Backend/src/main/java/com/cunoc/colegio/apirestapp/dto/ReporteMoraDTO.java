package com.cunoc.colegio.apirestapp.dto;

public class ReporteMoraDTO {
    private String tituloLibro;
    private String estudiantePoseedor;
    private String fechaEsperada;
    private int diasRetraso;
    private double recargoFinanciero;

    
    public ReporteMoraDTO() {
    }
    

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public String getEstudiantePoseedor() {
        return estudiantePoseedor;
    }

    public void setEstudiantePoseedor(String estudiantePoseedor) {
        this.estudiantePoseedor = estudiantePoseedor;
    }

    public String getFechaEsperada() {
        return fechaEsperada;
    }

    public void setFechaEsperada(String fechaEsperada) {
        this.fechaEsperada = fechaEsperada;
    }

    public int getDiasRetraso() {
        return diasRetraso;
    }

    public void setDiasRetraso(int diasRetraso) {
        this.diasRetraso = diasRetraso;
    }

    public double getRecargoFinanciero() {
        return recargoFinanciero;
    }

    public void setRecargoFinanciero(double recargoFinanciero) {
        this.recargoFinanciero = recargoFinanciero;
    }
    
}