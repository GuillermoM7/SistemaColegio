import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DirectorPersonalService } from '../../services/director-personal.service';
import { Paginacion } from '../../compartido/paginacion/paginacion';
import { RegistroPersonalComponent } from './registro-personal/registro-personal';
import { ModificarPersonalComponent } from './modificar-personal/modificar-personal';
import { GestionPersonalService } from '../../services/gestion-personal';

@Component({
  selector: 'app-director-personal',
  standalone: true,
  imports: [CommonModule, Paginacion, RegistroPersonalComponent, ModificarPersonalComponent],
  templateUrl: './director-personal.component.html',
  styleUrl: './director-personal.component.css'
})

export class DirectorPersonalComponent implements OnInit {
  listaPersonal: any[] = [];
  paginaActual: number = 1;
  totalPaginas: number = 1;
  totalRegistros: number = 0;

  mostrarFormulario: boolean = false;
  empleadoSeleccionado: any = null;

  mensajeExito: string = '';


  constructor(
    private directorService: DirectorPersonalService,
    private gestionService: GestionPersonalService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarPagina();
  }

  cargarPagina() {
    this.directorService.listarPersonalOperativo(this.paginaActual, 5).subscribe({
      next: (res) => {
        this.listaPersonal = res.empleados || [];
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
  

  abrirModal(emp: any) {
    this.empleadoSeleccionado = emp; 
  }


  cerrarModal() {
    this.empleadoSeleccionado = null;
  }


  refrescarTrasGuardar(mensaje: string) {
    this.mensajeExito = mensaje;
    this.cerrarFormulario();
    this.cerrarModal();
    this.cargarPagina();    
    setTimeout(() => {
      this.mensajeExito = '';
      this.cdr.detectChanges(); 
    }, 2000);
  }


  cambiarEstado(empleado: any) {
    const nuevoEstado = !empleado.estado;
    
    this.gestionService.cambiarEstadoEmpleado(empleado.idUsuario, nuevoEstado).subscribe({
      next: () => {
        this.mensajeExito = `El empleado fue ${nuevoEstado ? 'activado' : 'desactivado'} con éxito.`;
        this.cargarPagina(); 
        setTimeout(() => {
          this.mensajeExito = '';
          this.cdr.detectChanges(); 
        }, 2000);
      },
      error: (err) => console.error('Error al cambiar estado', err)
    });
  }
  
}