import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { EstudianteOperativoService } from '../../services/estudiante-operativo.service';

@Component({
  selector: 'app-mi-perfil',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mi-perfil.component.html'
})

export class MiPerfilComponent implements OnInit {
  perfil: any = null;
  cargando: boolean = true;
  errorMensaje: string = '';


  constructor(
    private estudianteOperativoService: EstudianteOperativoService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    const usuarioGuardado = localStorage.getItem('usuarioLogueado');
    if (usuarioGuardado) {
      const datosBackend = JSON.parse(usuarioGuardado);
      try {
        const tokenDecodificado: any = jwtDecode(datosBackend.token);
        this.cargarPerfil(tokenDecodificado.idUsuario);
      } catch (error) {
        this.router.navigate(['/login']);
      }
    } else {
      this.router.navigate(['/login']);
    }
  }


  cargarPerfil(idUsuario: number) {
    this.estudianteOperativoService.obtenerPerfil(idUsuario).subscribe({
      next: (res) => {
        this.perfil = res;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMensaje = err.error?.mensaje || 'No se pudo cargar la información del perfil.';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

}