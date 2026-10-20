package com.cunoc.colegio.apirestapp.dto;

public class ListadoEmpleadoDTO {
    private int idUsuario;
    private String correo;
    private int idRol;
    private boolean estado; 
    
    private int idEmpleado;
    private String nombre;
    private String apellidos;
    private String puesto;
    private String fechaContratacion;
    private Double salario;

    public ListadoEmpleadoDTO() {
    }

    public ListadoEmpleadoDTO(int idUsuario, String correo, int idRol, boolean estado, int idEmpleado, String nombre, String apellidos, String puesto, String fechaContratacion, Double salario) {
        this.idUsuario = idUsuario;
        this.correo = correo;
        this.idRol = idRol;
        this.estado = estado;
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.puesto = puesto;
        this.fechaContratacion = fechaContratacion;
        this.salario = salario;
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

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(String fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    

}