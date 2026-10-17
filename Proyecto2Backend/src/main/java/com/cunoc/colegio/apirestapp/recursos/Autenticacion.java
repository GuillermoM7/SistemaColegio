package com.cunoc.colegio.apirestapp.recursos;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cunoc.colegio.apirestapp.dto.CredencialesDTO;
import com.cunoc.colegio.apirestapp.dto.RespuestaLoginDTO;
import com.cunoc.colegio.database.daos.EmpleadoDAO;
import com.cunoc.colegio.database.daos.EstudianteDAO;
import com.cunoc.colegio.database.daos.UsuarioDAO;
import com.cunoc.colegio.database.entidades.Empleado;
import com.cunoc.colegio.database.entidades.Estudiante;
import com.cunoc.colegio.database.entidades.Usuario;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@Path("/auth")
public class Autenticacion {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON) 
    @Produces(MediaType.APPLICATION_JSON) 
    
    public Response iniciarSesion(CredencialesDTO credenciales) {
        
        UsuarioDAO dao = new UsuarioDAO();
        
        Usuario usuario = dao.autenticar(credenciales.getCorreo(), credenciales.getPassword());

        if (usuario != null) {
    
            String nombreParaFrontend = "Usuario Desconocido";
            List<Integer> rolesEmpleado = Arrays.asList(1, 2, 3, 4, 6, 7);

            if (usuario.getIdRol() == 5) { 
                EstudianteDAO estDao = new EstudianteDAO();
                Estudiante estudiante = estDao.obtenerEstudiantePorIdUsuario(usuario.getIdUsuario());
        
                if (estudiante != null) {
                    nombreParaFrontend = estudiante.getNombres() + " " + estudiante.getApellidos(); 
                }
                
            } else if (rolesEmpleado.contains(usuario.getIdRol())) { 
                EmpleadoDAO empDao = new EmpleadoDAO();
                Empleado empleado = empDao.obtenerEmpleadoPorIdUsuario(usuario.getIdUsuario());
                
                if (empleado != null) {
                    nombreParaFrontend = empleado.getNombres() + " " + empleado.getApellidos();
                }

            }

            Algorithm algoritmo = Algorithm.HMAC256("ClaveSecretaColegio2026");
            String tokenGenerado = JWT.create()
                    .withIssuer("SistemaColegio")
                    .withClaim("idUsuario", usuario.getIdUsuario())
                    .withClaim("idRol", usuario.getIdRol())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                    .sign(algoritmo);


            RespuestaLoginDTO dtoSeguro = new RespuestaLoginDTO(tokenGenerado, nombreParaFrontend);
            return Response.ok(dtoSeguro).build();
            
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"mensaje\": \"Credenciales incorrectas o usuario inactivo\"}")
                    .build();
        }
    }
}