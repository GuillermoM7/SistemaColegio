import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormularioEmpleado } from './formulario-empleado/formulario-empleado';
import { Paginacion } from '../../compartido/paginacion/paginacion';
import { GestionPersonalService,ListadoEmpleadoDTO } from '../../services/gestion-personal';
import { ModalEditarEmpleado } from './modal-editar-empleado/modal-editar-empleado';

@Component({
  selector: 'app-gestion-personal',
  standalone: true,
  imports: [CommonModule, FormularioEmpleado, Paginacion, ModalEditarEmpleado], 
  templateUrl: './gestion-personal.html',
  styleUrl: './gestion-personal.css'
})
export class GestionPersonal implements OnInit {
  mostrarFormulario: boolean = false;
  mensajeExitoPadre: string = '';
  mostrarModalEdicion: boolean = false;
  empleadoSeleccionadoParaEditar!: ListadoEmpleadoDTO;

  paginaActual: number = 1;
  totalPaginas: number = 1; 
  totalRegistros: number = 0;

  listaEmpleados: ListadoEmpleadoDTO[] = [];

  constructor(private gestionService: GestionPersonalService, private cdr: ChangeDetectorRef) {}


  ngOnInit(): void {
    this.cargarPagina();
  }


  cargarPagina() {
    this.gestionService.listarEmpleados(this.paginaActual, 5).subscribe({
      next: (respuesta) => {
        this.listaEmpleados = respuesta.empleados || [];
        this.totalRegistros = respuesta.totalRegistros || 0;
        this.totalPaginas = respuesta.totalPaginas || 0;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error al cargar la lista', err)
    });
  }
  

  abrirFormulario() {
    this.mostrarFormulario = true;
    this.mensajeExitoPadre = '';
  }


  cerrarFormulario() {
    this.mostrarFormulario = false;
  }


  manejarGuardadoExitoso(mensaje: string) {
    this.mostrarFormulario = false; 
    this.mensajeExitoPadre = mensaje; 
    this.cargarPagina()
  }


  manejarCambioPagina(nuevaPagina: number) {
    this.paginaActual = nuevaPagina;
    this.cargarPagina()
  }


  cambiarEstado(empleado: ListadoEmpleadoDTO) {
    const nuevoEstado = !empleado.estado; 
    const accionTexto = nuevoEstado ? 'ACTIVAR' : 'DESACTIVAR';

    if (confirm(`¿Estás seguro de que deseas ${accionTexto} al empleado ${empleado.nombre}?`)) {
      this.gestionService.cambiarEstadoEmpleado(empleado.idUsuario, nuevoEstado).subscribe({
        next: (respuesta) => {
          console.log(respuesta.mensaje);
          this.cargarPagina(); 
        },
        error: (err) => {
          console.error('Error al cambiar el estado', err);
          const mensajeError = err.error?.mensaje || 'Hubo un error al intentar cambiar el estado.';
          alert(mensajeError);
        }
      });
    }
  }


  abrirModalEdicion(empleado: ListadoEmpleadoDTO) {
    this.empleadoSeleccionadoParaEditar = empleado;
    this.mostrarModalEdicion = true;
  }


  cerrarModalEdicion() {
    this.mostrarModalEdicion = false;
  }


  guardarEdicionEmpleado(empleadoEditado: ListadoEmpleadoDTO) {
    this.gestionService.modificarEmpleado(empleadoEditado).subscribe({
      next: (respuesta) => {
        this.mensajeExitoPadre = respuesta.mensaje || 'Empleado actualizado correctamente';
        this.mostrarModalEdicion = false; 
        this.cargarPagina(); 
      },
      error: (err) => {
        console.error('Error al editar empleado', err);
        const msj = err.error?.mensaje || 'Error interno al intentar actualizar los datos.';
        alert(msj); 
      }
    });
  }  
}