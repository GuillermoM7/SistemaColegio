package com.cunoc.colegio.database.entidades;


public class Estudiante {
    
    private int idEstudiante;
    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String infoMedica;
    private String datosEncargados;
    private Estado estado;
    

    public Estudiante() {
    }

    
    public Estudiante(int idEstudiante, int idUsuario, String nombres, String apellidos, String infoMedica, String datosEncargados, Estado estado) {
        this.idEstudiante = idEstudiante;
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.infoMedica = infoMedica;
        this.datosEncargados = datosEncargados;
        this.estado = estado;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    
}
