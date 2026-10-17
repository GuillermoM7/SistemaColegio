package com.cunoc.colegio.apirestapp.dto;

public class RespuestaLoginDTO {
    
    private String token;
    private String nombre;

    public RespuestaLoginDTO() {
    }

    public RespuestaLoginDTO(String token, String nombre) {
        this.token = token;
        this.nombre = nombre;
    }

    
    
    public RespuestaLoginDTO(String token) {
        this.token = token;
    }

    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombres) {
        this.nombre = nombres;
    }
         
    
}
