package com.cunoc.colegio.apirestapp.dto;

public class RegistroEmpleadoDTO {
    
    private String correo;
    private String password;
    private int idRol;
    
    private String nombres;
    private String apellidos;
    private String puesto;
    private String fechaContratacion; 
    private Double salario;
    
    
    public RegistroEmpleadoDTO() {
        
    }

    
    public RegistroEmpleadoDTO(String correo, String password, int idRol, String nombres, String apellidos, String puesto, String fechaContratacion, Double salario) {
        this.correo = correo;
        this.password = password;
        this.idRol = idRol;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.puesto = puesto;
        this.fechaContratacion = fechaContratacion;
        this.salario = salario;
    }

    
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
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