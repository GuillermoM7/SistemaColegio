package com.cunoc.colegio.database.entidades;

import java.time.LocalDate;

public class Empleado {
    
    private int idEmpleado;
    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String puesto;
    private LocalDate fechaContratacion;
    private double salario;

    
    public Empleado() {
    }

    
    public Empleado(int idEmpleado, int idUsuario, String Nombres, String Apellidos, String Puesto, LocalDate fechaContratacion, double salario) {
        this.idEmpleado = idEmpleado;
        this.idUsuario = idUsuario;
        this.nombres = Nombres;
        this.apellidos = Apellidos;
        this.puesto = Puesto;
        this.fechaContratacion = fechaContratacion;
        this.salario = salario;
    }

    
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
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

    public void setNombres(String Nombres) {
        this.nombres = Nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String Apellidos) {
        this.apellidos = Apellidos;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String Puesto) {
        this.puesto = Puesto;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
    
    
}
