import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { jwtDecode } from 'jwt-decode';
import { SecretariaEstudianteService } from '../../services/secretaria-estudiante.service';
import { FormularioEstudianteComponent } from './formulario-estudiante.component/formulario-estudiante.component';
import { PanelCobrosComponent } from './panel-cobros.component/panel-cobros.component';
import { ExpedienteAlumnoComponent } from './expediente-alumno.component/expediente-alumno.component';

@Component({
  selector: 'app-gestion-estudiantes',
  standalone: true,
  imports: [CommonModule, FormsModule, FormularioEstudianteComponent, PanelCobrosComponent, ExpedienteAlumnoComponent],
  templateUrl: './gestion-estudiantes.component.html'
})

export class GestionEstudiantesComponent {
  terminoBusqueda: string = '';
  resultadosBusqueda: any[] = [];
  estudianteEnFoco: any = null;
  
  vistaActual: number = 1; 
  mensajeExito: string = '';
  idUsuarioLogueado: number = 0;


  constructor(private secretariaService: SecretariaEstudianteService, private cdr: ChangeDetectorRef) {}


  ngOnInit() {
    const usuarioGuardado = localStorage.getItem('usuarioLogueado');
    if (usuarioGuardado) {
      try {
        const datosBackend = JSON.parse(usuarioGuardado);
        const tokenDecodificado: any = jwtDecode(datosBackend.token);
        this.idUsuarioLogueado = tokenDecodificado.idUsuario; 
      } catch (error) {
        console.error('Error al leer el token en gestión de estudiantes', error);
      }
    }
  }


  buscar() {
    if (!this.terminoBusqueda.trim()) {
      this.resultadosBusqueda = [];
      return;
    }
    this.secretariaService.buscar(this.terminoBusqueda).subscribe(res => {
      this.resultadosBusqueda = res;
      this.cdr.detectChanges();
    });
  }


  seleccionarEstudiante(est: any) {
    this.estudianteEnFoco = est;
    this.resultadosBusqueda = [];
    this.terminoBusqueda = '';
    this.cdr.detectChanges()
  }


  cambiarEstadoEstudiante(nuevoEstado: string) {
    this.secretariaService.cambiarEstado(this.estudianteEnFoco.idEstudiante, nuevoEstado).subscribe({
      next: (res) => {
        this.estudianteEnFoco.estado = nuevoEstado;
        this.mostrarMensaje(res.mensaje);
      }
    });
  }


  abrirCreacion() {
    this.estudianteEnFoco = null; 
    this.vistaActual = 2;
  }


  abrirEdicion() {
    this.vistaActual = 2;
  }


  volverAlDashboard(mensaje?: string | void) {
    this.vistaActual = 1;
    if (typeof mensaje === 'string') {
      this.mostrarMensaje(mensaje);
      if (this.estudianteEnFoco) {
        this.secretariaService.buscar(this.estudianteEnFoco.nombres).subscribe(res => {
          const actualizado = res.find(e => e.idEstudiante === this.estudianteEnFoco.idEstudiante);
          if (actualizado) this.estudianteEnFoco = actualizado;
        });
      }
    }
  }


  mostrarMensaje(msg: string) {
    this.mensajeExito = msg;
    this.cdr.detectChanges();
    setTimeout(() => {
      this.mensajeExito = '';
      this.cdr.detectChanges();
    }, 3000);
  }


  abrirPagos() {
    this.vistaActual = 3;
    this.cdr.detectChanges();
  }

  
  abrirExpediente() {
    this.vistaActual = 4;
    this.cdr.detectChanges();
  }  

}