package com.cunoc.colegio.apirestapp.recursos;

import com.cunoc.colegio.apirestapp.dto.CredencialesDTO;
import com.cunoc.colegio.apirestapp.dto.RespuestaLoginDTO;
import com.cunoc.colegio.database.daos.UsuarioDAO;
import com.cunoc.colegio.database.entidades.Usuario;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class Autenticacion {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON) 
    @Produces(MediaType.APPLICATION_JSON) 
    
    public Response iniciarSesion(CredencialesDTO credenciales) {
        
        UsuarioDAO dao = new UsuarioDAO();
        
        Usuario usuario = dao.autenticar(credenciales.getUsername(), credenciales.getPassword());

        if (usuario != null) {
            RespuestaLoginDTO dtoSeguro = new RespuestaLoginDTO(usuario.getUsername(), usuario.getIdRol());
            return Response.ok(dtoSeguro).build();
            
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"mensaje\": \"Credenciales incorrectas o usuario inactivo\"}")
                    .build();
        }
    }
}