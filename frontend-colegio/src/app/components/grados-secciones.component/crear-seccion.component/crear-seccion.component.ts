import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DirectorAcademicoService } from '../../../services/director-academico.service';


@Component({
  selector: 'app-crear-seccion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './crear-seccion.component.html'
})

export class CrearSeccionComponent implements OnInit {

  @Input() gradoEnFoco: any;
  @Output() onVolver = new EventEmitter<string | void>();

  nombreSeccion: string = '';
  cursos: any[] = [];
  maestros: any[] = [];
  
  esperandoConfirmacion: boolean = false;
  mensajeError: string = '';


  constructor(
    private directorService: DirectorAcademicoService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    this.cargarMaestros();
    this.cargarCursos();
  }


  cargarMaestros() {
    this.directorService.obtenerMaestros().subscribe(res => {
      this.maestros = res;
      this.cdr.detectChanges();
    });
  }


  cargarCursos() {
    this.directorService.obtenerCursosCurriculoActivo(this.gradoEnFoco.idGrado).subscribe(res => {
      this.cursos = res.map((c: any) => ({ ...c, idEmpleado: 0 }));
      this.cdr.detectChanges();
    });
  }


  get formularioValido(): boolean {
    if (!this.nombreSeccion.trim()) return false;
    if (this.cursos.length === 0) return false;
    return this.cursos.every(c => c.idEmpleado > 0);
  }

  
  iniciarGuardado() {
    this.esperandoConfirmacion = true;
  }


  cancelarConfirmacion() {
    this.esperandoConfirmacion = false;
  }


  confirmarGuardado() {
    const payload = {
      idGrado: this.gradoEnFoco.idGrado,
      nombreSeccion: this.nombreSeccion,
      asignaciones: this.cursos.map(c => ({
        idCurso: c.idCurso,
        idEmpleado: Number(c.idEmpleado)
      }))
    };
    this.directorService.guardarSeccion(payload).subscribe({
      next: () => {
        this.onVolver.emit(`Sección '${this.nombreSeccion.toUpperCase()}' creada y maestros asignados exitosamente.`);
      },
      error: (err) => {
        this.mensajeError = err.error?.mensaje || 'Error al guardar la sección.';
        this.esperandoConfirmacion = false;
        this.cdr.detectChanges();
      }
    });
  }

}