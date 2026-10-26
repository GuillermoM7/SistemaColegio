import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SecretariaEstudianteService } from '../../../services/secretaria-estudiante.service';

@Component({
  selector: 'app-formulario-estudiante',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './formulario-estudiante.component.html'
})

export class FormularioEstudianteComponent implements OnInit {
  @Input() estudianteEdicion: any = null;
  @Output() onVolver = new EventEmitter<string | void>();

  estudiante = {
    nombres: '',
    apellidos: '',
    infoMedica: '',
    datosEncargados: ''
  };

  mensajeError: string = '';


  constructor(private secretariaService: SecretariaEstudianteService) {}


  ngOnInit() {
    if (this.estudianteEdicion) {
      this.estudiante = { ...this.estudianteEdicion };
    }
  }


  guardar() {
    if (!this.estudiante.nombres || !this.estudiante.apellidos || !this.estudiante.datosEncargados) {
      this.mensajeError = 'Nombres, Apellidos y el ID del Encargado son obligatorios.';
      return;
    }
    if (this.estudianteEdicion) {
      this.secretariaService.actualizar(this.estudianteEdicion.idEstudiante, this.estudiante).subscribe({
        next: (res) => this.onVolver.emit(res.mensaje),
        error: (err) => this.mensajeError = err.error?.mensaje || 'Error al actualizar.'
      });
    } else {
      this.secretariaService.crear(this.estudiante).subscribe({
        next: (res) => this.onVolver.emit(res.mensaje),
        error: (err) => this.mensajeError = err.error?.mensaje || 'Error al crear.'
      });
    }
  }
}