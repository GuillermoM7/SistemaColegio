import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DirectorPersonalService } from '../../../services/director-personal.service';

@Component({
  selector: 'app-modificar-personal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modificar-personal.html'
})
export class ModificarPersonalComponent implements OnInit {
  @Input() empleadoBase: any;
  @Output() onCancelar = new EventEmitter<void>();
  @Output() onGuardado = new EventEmitter<void>();

  empleadoEditando: any = {};
  mensajeError: string = '';

  constructor(private directorService: DirectorPersonalService) {}

  ngOnInit() {
    this.empleadoEditando = { ...this.empleadoBase }; 
  }

    guardar() {
      if (this.empleadoEditando.salario != null && this.empleadoEditando.salario < 0) {
        this.mensajeError = 'El salario mensual no puede ser negativo.';
        return;
      }

      this.directorService.modificarPersonalOperativo(this.empleadoEditando).subscribe({
        next: () => this.onGuardado.emit(),
        error: () => this.mensajeError = 'Error al actualizar los datos.'
      });
    }
}