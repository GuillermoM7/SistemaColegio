import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DirectorAlumnoService } from '../../services/director-alumno.service';
import { Paginacion } from '../../compartido/paginacion/paginacion';
import { RegistroUsuarioAlumnoComponent } from './registro-usuario-alumno.component/registro-usuario-alumno.component';

@Component({
  selector: 'app-director-alumnos',
  standalone: true,
  imports: [CommonModule, Paginacion, RegistroUsuarioAlumnoComponent],
  templateUrl: './director-alumnos.component.html'
})
export class DirectorAlumnosComponent implements OnInit {
  listaAlumnos: any[] = [];
  paginaActual: number = 1;
  totalPaginas: number = 1;
  totalRegistros: number = 0;

  mostrarFormulario: boolean = false;
  mensajeExito: string = '';

  constructor(
    private alumnoService: DirectorAlumnoService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarPagina();
  }


  cargarPagina() {
    this.alumnoService.listarPaginados(this.paginaActual, 5).subscribe({
      next: (res) => {
        this.listaAlumnos = res.alumnos || [];
        this.totalRegistros = res.totalRegistros || 0;
        this.totalPaginas = res.totalPaginas || 0;
        this.cdr.detectChanges();
      }
    });
  }


  alCambiarPagina(nuevaPagina: number) {
    this.paginaActual = nuevaPagina;
    this.cargarPagina();
  }


  abrirFormulario() { 
    this.mostrarFormulario = true; 
  }


  cerrarFormulario() { 
    this.mostrarFormulario = false; 
  }


  cambiarEstado(alumno: any) {
    const nuevoEstado = !alumno.estado;
    this.alumnoService.cambiarEstado(alumno.idUsuario, nuevoEstado).subscribe({
      next: () => {
        this.refrescarTrasGuardar(`El acceso del estudiante fue ${nuevoEstado ? 'activado' : 'desactivado'}.`);
      }
    });
  }


  refrescarTrasGuardar(mensaje: string) {
    this.mensajeExito = mensaje;
    this.cerrarFormulario();
    this.cargarPagina(); 
    
    setTimeout(() => {
      this.mensajeExito = '';
      this.cdr.detectChanges();
    }, 3000);
  }
  
}