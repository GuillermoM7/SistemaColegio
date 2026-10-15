package com.cunoc.colegio.apirestapp.dto;

public class CredencialesDTO {
    private String username;
    private String password; 

    
    public CredencialesDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}