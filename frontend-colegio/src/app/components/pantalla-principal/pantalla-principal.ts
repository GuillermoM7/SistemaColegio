import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-pantalla-principal',
  standalone: true,
  imports: [],
  templateUrl: './pantalla-principal.html',
  styleUrl: './pantalla-principal.css'
})

export class PantallaPrincipal implements OnInit {
  nombreUsuario: string = ''; 
  idRolUsuario: number = 0;

  constructor(private router: Router) {}

  ngOnInit() {
    const usuarioGuardado = localStorage.getItem('usuarioLogueado');

    if (usuarioGuardado) {
      const datosBackend = JSON.parse(usuarioGuardado);
      this.nombreUsuario = datosBackend.nombre;
      try {
        const tokenDecodificado: any = jwtDecode(datosBackend.token);
        this.idRolUsuario = tokenDecodificado.idRol; 
      } catch (error) {
        console.error('Error al leer el token', error);
      }

    } else {
      this.router.navigate(['/login']);
    }
  }

  cerrarSesion() {
    localStorage.removeItem('usuarioLogueado');
    this.router.navigate(['/login']);
  }
}