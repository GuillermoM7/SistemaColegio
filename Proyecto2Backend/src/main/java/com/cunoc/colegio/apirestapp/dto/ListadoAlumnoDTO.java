package com.cunoc.colegio.apirestapp.dto;

public class ListadoAlumnoDTO {
    private int idUsuario;
    private int idEstudiante;
    private String nombres;
    private String apellidos;
    private String correo;
    
    private String grado;
    private String nivel;
    private String carrera;
    
    private boolean estado;

    
    public ListadoAlumnoDTO() {
    }

    
    public ListadoAlumnoDTO(int idUsuario, int idEstudiante, String nombres, String apellidos, String correo, String grado, String nivel, String carrera, boolean estado) {
        this.idUsuario = idUsuario;
        this.idEstudiante = idEstudiante;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.grado = grado;
        this.nivel = nivel;
        this.carrera = carrera;
        this.estado = estado;
    }
    
     
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
}
