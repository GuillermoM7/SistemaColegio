package com.cunoc.colegio.apirestapp.dto;

public class RecuperacionDTO {
    private String correo;
    private String nuevaPassword;

    
    public RecuperacionDTO() {
    }

    
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNuevaPassword() {
        return nuevaPassword;
    }

    public void setNuevaPassword(String nuevaPassword) {
        this.nuevaPassword = nuevaPassword;
    }
    
    
}