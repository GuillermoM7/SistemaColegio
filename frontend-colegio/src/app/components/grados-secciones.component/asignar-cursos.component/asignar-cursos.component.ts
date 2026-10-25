import { Component, Input, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DirectorCursoService } from '../../../services/director-curso.service';
import { DirectorAcademicoService } from '../../../services/director-academico.service';

@Component({
  selector: 'app-asignar-cursos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './asignar-cursos.component.html'
})

export class AsignarCursosComponent {
  
  @Input() gradoEnFoco: any;
  @Output() onVolver = new EventEmitter<string |void>();

  terminoBusquedaCurso: string = '';
  resultadosCursos: any[] = [];
  cursosSeleccionados: any[] = [];
  mensajeErrorCurriculo: string = '';
  esperandoConfirmacion: boolean = false;


  constructor(
    private cursoService: DirectorCursoService,
    private directorService: DirectorAcademicoService,
    private cdr: ChangeDetectorRef
  ) {}


  buscarCursos() {
    if (!this.terminoBusquedaCurso.trim()) {
      this.resultadosCursos = [];
      return;
    }   
    this.cursoService.listarPaginados(1, 10, this.terminoBusquedaCurso).subscribe({
      next: (res) => {
        this.resultadosCursos = res.cursos || [];
        this.cdr.detectChanges();
      }
    });
  }


  seleccionarCurso(curso: any) {
    const yaExiste = this.cursosSeleccionados.find(c => c.idCurso === curso.idCurso);
    if (!yaExiste) {
      this.cursosSeleccionados.push(curso);
    }
    this.resultadosCursos = []; 
    this.terminoBusquedaCurso = '';
  }


  quitarCurso(idCurso: number) {
    this.cursosSeleccionados = this.cursosSeleccionados.filter(c => c.idCurso !== idCurso);
  }


  guardarCurriculo() {
    if (this.cursosSeleccionados.length === 0) {
      this.mensajeErrorCurriculo = 'Agregue al menos un curso a la lista.';
      return;
    }
    this.esperandoConfirmacion = true; 
  }


  cancelarConfirmacion() {
    this.esperandoConfirmacion = false;
  }


  confirmarGuardado() {
    const payload = {
      idGrado: this.gradoEnFoco.idGrado,
      cursosIds: this.cursosSeleccionados.map(c => c.idCurso)
    };
    this.directorService.guardarCurriculo(payload).subscribe({
      next: () => {
        this.onVolver.emit('¡Currículo creado y cursos asignados con éxito!');
      },
      error: (err) => {
        this.mensajeErrorCurriculo = err.error?.mensaje || 'Error al guardar el currículo.';
        this.esperandoConfirmacion = false;
        this.cdr.detectChanges();
      }
    });
  }
}