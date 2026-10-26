package com.cunoc.colegio.apirestapp.dto;

public class EstudianteDTO {
    private int idEstudiante;
    private Integer idUsuario;
    private String nombres;
    private String apellidos;
    private String infoMedica;
    private String datosEncargados; 
    private String estado;

    
    public EstudianteDTO() {
    }

    
    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getInfoMedica() {
        return infoMedica;
    }

    public void setInfoMedica(String infoMedica) {
        this.infoMedica = infoMedica;
    }

    public String getDatosEncargados() {
        return datosEncargados;
    }

    public void setDatosEncargados(String datosEncargados) {
        this.datosEncargados = datosEncargados;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
   
    
}
