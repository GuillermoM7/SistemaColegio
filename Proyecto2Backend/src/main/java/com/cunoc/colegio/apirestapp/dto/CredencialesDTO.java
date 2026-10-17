package com.cunoc.colegio.apirestapp.dto;

public class CredencialesDTO {
    private String correo;
    private String password; 

    
    public CredencialesDTO() {
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
}