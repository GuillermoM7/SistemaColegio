import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { GestionPersonalService, RegistroEmpleadoDTO } from '../../../services/gestion-personal';

@Component({
  selector: 'app-formulario-empleado',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './formulario-empleado.html',
  styleUrl: './formulario-empleado.css'
})
export class FormularioEmpleado {
  
  @Output() onCancelar = new EventEmitter<void>();
  @Output() onGuardadoExito = new EventEmitter<string>();

  nuevoEmpleado: RegistroEmpleadoDTO = {
    correo: '', password: '', idRol: 0, nombres: '', apellidos: '', puesto: '', fechaContratacion: '', salario: 0
  };

  mensajeError: string = '';

  constructor(private gestion: GestionPersonalService) {}

  registrar() {
    this.mensajeError = '';

    if (!this.nuevoEmpleado.correo || !this.nuevoEmpleado.password || this.nuevoEmpleado.idRol === 0 || !this.nuevoEmpleado.nombres || !this.nuevoEmpleado.apellidos || !this.nuevoEmpleado.puesto || !this.nuevoEmpleado.fechaContratacion || !this.nuevoEmpleado.salario ) {
      this.mensajeError = 'Por favor, completa todos los campos.';
      return;
    }

    this.gestion.registrarEmpleado(this.nuevoEmpleado).subscribe({
      next: (respuesta) => {

        this.onGuardadoExito.emit(respuesta.mensaje || 'Empleado registrado correctamente.');
        this.limpiarFormulario(); 
      },
      error: (errorHttp) => {
        this.mensajeError = errorHttp.error?.mensaje || 'Error de conexión con el servidor.';
      }
    });
  }

  limpiarFormulario() {
    this.nuevoEmpleado = {
      correo: '', password: '', idRol: 0, nombres: '', apellidos: '', puesto: '', fechaContratacion: '', salario: 0
    };
  }

  cancelar() {
    this.limpiarFormulario();
    this.onCancelar.emit(); 
  }
}