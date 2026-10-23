import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DirectorPersonalService } from '../../../services/director-personal.service';

@Component({
  selector: 'app-registro-personal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './registro-personal.html',
  styleUrl: './registro-personal.css',
})
export class RegistroPersonalComponent {
  @Output() onCancelar = new EventEmitter<void>();
  @Output() onGuardado = new EventEmitter<void>();

  mensajeError: string = '';
  rolesPermitidos = [
    { id: 3, nombre: 'Secretaria' },
    { id: 4, nombre: 'Maestro' },
    { id: 6, nombre: 'Bibliotecario' },
    { id: 7, nombre: 'Mantenimiento'}
  ];

  nuevoEmpleado: any = {
    correo: '', password: '', idRol: 0, nombres: '', 
    apellidos: '', puesto: '', fechaContratacion: '', salario: null
  };

  constructor(private directorService: DirectorPersonalService) {}

    guardar() {
      if (this.nuevoEmpleado.salario != null && this.nuevoEmpleado.salario < 0) {
        this.mensajeError = 'El salario inicial no puede ser negativo.';
      return;
    }
    
    this.directorService.crearPersonalOperativo(this.nuevoEmpleado).subscribe({
      next: () => this.onGuardado.emit(),
      error: (err) => this.mensajeError = err.error?.mensaje || 'Error al registrar al empleado.'
    });
  }

}  