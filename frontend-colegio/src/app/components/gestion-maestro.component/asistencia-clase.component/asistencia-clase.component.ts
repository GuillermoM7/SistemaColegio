import { Component, Input, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, formatDate } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MaestroService } from '../../../services/maestro.service';

@Component({
  selector: 'app-asistencia-clase',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './asistencia-clase.component.html'
})

export class AsistenciaClaseComponent implements OnInit {
  @Input() cursoActual: any;
  @Input() nominaCompleta: any[] = [];

  fechaAsistencia: string = '';
  listaAsistencia: any[] = [];
  guardando: boolean = false;
  mensajeExito: string = '';
  mensajeError: string = '';


  constructor(
    private maestroService: MaestroService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    this.fechaAsistencia = formatDate(new Date(), 'yyyy-MM-dd', 'en-US');
    this.generarLista();
  }


  generarLista() {
    this.listaAsistencia = this.nominaCompleta.map(alumno => ({
      idEstudiante: alumno.idEstudiante,
      idAsignacion: this.cursoActual.idAsignacion,
      fecha: this.fechaAsistencia,
      estado: 'Presente', 
      nombres: alumno.nombres,
      apellidos: alumno.apellidos
    }));
  }


  onFechaCambiada() {
    this.listaAsistencia.forEach(item => item.fecha = this.fechaAsistencia);
  }


  guardar() {
    this.guardando = true;
    this.mensajeExito = '';
    this.mensajeError = '';

    const payload = this.listaAsistencia.map(item => ({
      idEstudiante: item.idEstudiante,
      idAsignacion: item.idAsignacion,
      fecha: item.fecha,
      estado: item.estado
    }));

    this.maestroService.guardarAsistencia(this.cursoActual.idAsignacion, payload).subscribe({
      next: (res: any) => {
        this.guardando = false;
        this.mensajeExito = '¡Asistencia registrada exitosamente!';
        this.cdr.detectChanges();
        setTimeout(() => { this.mensajeExito = ''; this.cdr.detectChanges(); }, 4000);
      },
      error: (err) => {
        this.guardando = false;
        this.mensajeError = 'Ocurrió un error al guardar la asistencia.';
        this.cdr.detectChanges();
      }
    });
  }

}