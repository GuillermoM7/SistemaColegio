import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';
import { SecretariaEstudianteService } from '../../services/secretaria-estudiante.service';
import { ExpedienteAlumnoComponent } from '../gestion-estudiantes.component/expediente-alumno.component/expediente-alumno.component';

@Component({
  selector: 'app-mis-notas',
  standalone: true,
  imports: [CommonModule, ExpedienteAlumnoComponent],
  templateUrl: './mis-notas.component.html'
})

export class MisNotasComponent implements OnInit {
  datosMiPerfil: any = null;
  cargando: boolean = true;
  errorAutenticacion: boolean = false;


  constructor(
    private estudianteService: SecretariaEstudianteService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}


  ngOnInit(): void {
    const usuarioGuardado = localStorage.getItem('usuarioLogueado');
    if (usuarioGuardado) {
      const datosBackend = JSON.parse(usuarioGuardado);
      try {
        const tokenDecodificado: any = jwtDecode(datosBackend.token);
        this.cargarDatosEstudiante(tokenDecodificado.idUsuario);
      } catch (error) {
        this.router.navigate(['/login']);
      }
    } else {
      this.router.navigate(['/login']);
    }
  }


  cargarDatosEstudiante(idUsuario: number) {
    this.estudianteService.obtenerEstudiantePorUsuario(idUsuario).subscribe({
      next: (res) => {
        this.datosMiPerfil = res; 
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorAutenticacion = true;
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }
  
}