import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-pantalla-principal',
  standalone: true,
  imports: [],
  templateUrl: './pantalla-principal.html',
  styleUrl: './pantalla-principal.css'
})
export class PantallaPrincipal implements OnInit{
  nombreUsuario = '';

  constructor(private router: Router) {}

  ngOnInit() {
    const usuarioGuardado = localStorage.getItem('usuarioLogueado');

    if (usuarioGuardado) {
      const usuario = JSON.parse(usuarioGuardado);
      this.nombreUsuario = usuario.username; 
    } else {
      this.router.navigate(['/login']);
    }
  }
  
  cerrarSesion() {
    localStorage.removeItem('usuarioLogueado');
    this.router.navigate(['/login']);
  }
}
