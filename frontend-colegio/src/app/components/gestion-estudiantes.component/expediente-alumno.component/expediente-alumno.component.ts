import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SecretariaEstudianteService } from '../../../services/secretaria-estudiante.service';

@Component({
  selector: 'app-expediente-alumno',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './expediente-alumno.component.html'
})

export class ExpedienteAlumnoComponent implements OnInit {
  @Input() estudiante: any;
  @Input() esVistaEstudiante: boolean = false; 
  @Output() onVolver = new EventEmitter<void>();

  historialCursos: any[] = [];
  cargando: boolean = true;
  aniosDisponibles: number[] = [];
  anioSeleccionado: number = 0;


  constructor(
    private estudianteService: SecretariaEstudianteService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    this.estudianteService.obtenerExpedienteAcademico(this.estudiante.idEstudiante).subscribe({
      next: (res) => {
        this.historialCursos = res;

        if (this.historialCursos.length > 0) {
          this.aniosDisponibles = [...new Set(this.historialCursos.map(c => c.anioLectivo))];
          this.anioSeleccionado = this.aniosDisponibles[0];
        }
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }


  get cursosFiltrados(): any[] {
    if (this.anioSeleccionado == 0) {
      return this.historialCursos; 
    }
    return this.historialCursos.filter(c => c.anioLectivo == this.anioSeleccionado);
  }
}