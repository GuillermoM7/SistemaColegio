package com.cunoc.colegio.apirestapp.dto;


public class RespuestaLoginDTO {
    
    private String username;
    private int idRol;

    public RespuestaLoginDTO() {
    }

    public RespuestaLoginDTO(String username, int idRol) {
        this.username = username;
        this.idRol = idRol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String usarname) {
        this.username = usarname;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
    
    
    
}
