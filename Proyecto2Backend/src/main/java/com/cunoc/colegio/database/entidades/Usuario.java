package com.cunoc.colegio.database.entidades;

public class Usuario {

    private int idUsuario;
    private String username;
    private String contrasena;
    private int idRol;
    private boolean activo; 


    public Usuario() {
    }


    public Usuario(int idUsuario, String username, String password, int idRol, boolean activo) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.contrasena = password;
        this.idRol = idRol;
        this.activo = activo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return contrasena;
    }

    public void setPassword(String password) {
        this.contrasena = password;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}