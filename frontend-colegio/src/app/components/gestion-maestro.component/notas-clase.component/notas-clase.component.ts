import { Component, Input, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MaestroService } from '../../../services/maestro.service';

@Component({
  selector: 'app-notas-clase',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './notas-clase.component.html'
})
export class NotasClaseComponent implements OnInit {
  @Input() cursoActual: any;
  @Input() nominaCompleta: any[] = [];

  // Variables de Actividades
  actividades: any[] = [];
  nuevaActividad = { nombreActividad: '', ponderacion: null };
  creando: boolean = false;

  // Variables de Calificación
  actividadSeleccionada: any = null;
  listaCalificaciones: any[] = [];
  guardando: boolean = false;
  
  mensajeExito: string = '';
  mensajeError: string = '';

  esExamenFinal: boolean = false;

  constructor(
    private maestroService: MaestroService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarActividades();
  }

  cargarActividades() {
    this.maestroService.obtenerActividades(this.cursoActual.idAsignacion).subscribe({
      next: (res) => {
        this.actividades = res;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error al cargar actividades', err)
    });
  }

  crearNuevaActividad() {
    if (!this.nuevaActividad.nombreActividad || !this.nuevaActividad.ponderacion) return;
    
    this.creando = true;
    this.maestroService.crearActividad(this.cursoActual.idAsignacion, this.nuevaActividad).subscribe({
      next: () => {
        this.creando = false;
        this.nuevaActividad = { nombreActividad: '', ponderacion: null };
        this.mostrarMensaje('¡Actividad creada exitosamente!', false);
        this.cargarActividades();
      },
      error: () => {
        this.creando = false;
        this.mostrarMensaje('Error al crear la actividad.', true);
      }
    });
  }

  seleccionarActividad(actividad: any) {
    this.actividadSeleccionada = actividad;
    this.esExamenFinal = false;

    this.listaCalificaciones = this.nominaCompleta.map(alumno => ({
      idEstudiante: alumno.idEstudiante,
      idActividad: actividad.idActividad,
      nombres: alumno.nombres,
      apellidos: alumno.apellidos,
      punteoObtenido: 0 
    }));

    this.maestroService.obtenerCalificacionesActividad(actividad.idActividad).subscribe({
      next: (notasGuardadas) => {
        if (notasGuardadas && notasGuardadas.length > 0) {
          this.listaCalificaciones.forEach(item => {
            const notaPrevia = notasGuardadas.find(n => n.idEstudiante === item.idEstudiante);
            if (notaPrevia) {
              item.punteoObtenido = notaPrevia.punteoObtenido; 
            }
          });
        }
        this.cdr.detectChanges(); 
      },
      error: (err) => console.error('Error al cargar notas previas', err)
    });
  }


  guardarCalificaciones() {
    this.guardando = true;

    if (this.esExamenFinal) {
      const payloadExamen = this.listaCalificaciones.map(item => ({
        idEstudiante: item.idEstudiante,
        notaExamen: item.punteoObtenido
      }));

      this.maestroService.guardarNotasExamenFinal(this.cursoActual.idAsignacion, payloadExamen).subscribe({
        next: () => {
          this.guardando = false;
          this.actividadSeleccionada = null;
          this.mostrarMensaje('¡Examen final guardado correctamente!', false);
        },
        error: () => { this.guardando = false; this.mostrarMensaje('Error al guardar examen.', true); }
      });

    } else {
      const payloadActividad = this.listaCalificaciones.map(item => ({
        idEstudiante: item.idEstudiante,
        idActividad: item.idActividad, 
        punteoObtenido: item.punteoObtenido
      }));

      this.maestroService.guardarCalificaciones(this.cursoActual.idAsignacion, payloadActividad).subscribe({
        next: () => {
          this.guardando = false;
          this.actividadSeleccionada = null;
          this.mostrarMensaje('¡Calificaciones guardadas y zona actualizada!', false);
        },
        error: () => { this.guardando = false; this.mostrarMensaje('Error al guardar las calificaciones.', true); }
      });
    }
  }


  mostrarMensaje(msg: string, esError: boolean) {
    if (esError) {
      this.mensajeError = msg;
      this.mensajeExito = '';
    } else {
      this.mensajeExito = msg;
      this.mensajeError = '';
    }
    this.cdr.detectChanges();
    setTimeout(() => { this.mensajeExito = ''; this.mensajeError = ''; this.cdr.detectChanges(); }, 4000);
  }


  seleccionarExamenFinal() {
    this.esExamenFinal = true;
    this.actividadSeleccionada = { nombreActividad: 'Examen Final (Total)', ponderacion: 100 }; 
    
    this.listaCalificaciones = this.nominaCompleta.map(alumno => ({
      idEstudiante: alumno.idEstudiante,
      nombres: alumno.nombres,
      apellidos: alumno.apellidos,
      punteoObtenido: 0 
    }));

    this.maestroService.obtenerNotasExamenFinal(this.cursoActual.idAsignacion).subscribe({
      next: (notas) => {
        if (notas && notas.length > 0) {
          this.listaCalificaciones.forEach(item => {
            const notaPrevia = notas.find(n => n.idEstudiante === item.idEstudiante);
            if (notaPrevia) item.punteoObtenido = notaPrevia.notaExamen;
          });
        }
        this.cdr.detectChanges();
      }
    });
  }

}