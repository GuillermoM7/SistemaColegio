import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DirectorCursoService } from '../../../services/director-curso.service';

@Component({
  selector: 'app-crear-curso',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './crear-curso.component.html'
})
export class CrearCursoComponent {
  @Output() onCancelar = new EventEmitter<void>();
  @Output() onGuardado = new EventEmitter<void>();

  nombreCurso: string = '';
  mensajeError: string = '';

  constructor(private cursoService: DirectorCursoService) {}

  
  guardar() {
    if (!this.nombreCurso || this.nombreCurso.trim().length === 0) {
      this.mensajeError = 'El nombre del curso es obligatorio.';
      return;
    }
    this.cursoService.crearCurso({ nombre: this.nombreCurso }).subscribe({
      next: () => this.onGuardado.emit(),
      error: (err) => this.mensajeError = err.error?.mensaje || 'Error al crear el curso.'
    });
  }
  
}