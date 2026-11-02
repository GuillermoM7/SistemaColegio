import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SecretariaEstudianteService } from '../../services/secretaria-estudiante.service';
import { ReportesService } from '../../services/reportes.service';

@Component({
  selector: 'app-historial-pagos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './historial-pagos.component.html'
})

export class HistorialPagosComponent {

  terminoBusqueda: string = '';
  resultadosBusqueda: any[] = [];
  estudianteSeleccionado: any = null;

  fechaInicio: string = '';
  fechaFin: string = '';

  cargandoPDF: boolean = false;


  constructor(
    private estudianteService: SecretariaEstudianteService,
    private reportesService: ReportesService,
    private cdr: ChangeDetectorRef
  ) {}


  buscarEstudiante() {
    if (!this.terminoBusqueda.trim()) {
      this.resultadosBusqueda = [];
      return;
    }
    this.estudianteService.buscar(this.terminoBusqueda).subscribe(res => {
      this.resultadosBusqueda = res.filter((e: any) => e.estado === 'Activo');
      this.cdr.detectChanges();
    });
  }


  seleccionarEstudiante(est: any) {
    this.estudianteSeleccionado = est;
    this.resultadosBusqueda = [];
    this.terminoBusqueda = '';
    this.cdr.detectChanges();
  }


  deseleccionarEstudiante() {
    this.estudianteSeleccionado = null;
    this.cdr.detectChanges();
  }


  get formularioValido(): boolean {
    return this.estudianteSeleccionado !== null;
  }


  generarPDF() {
    if (!this.formularioValido) return;
    
    this.cargandoPDF = true;
    this.reportesService.obtenerHistorialPagos(this.estudianteSeleccionado.idEstudiante, this.fechaInicio, this.fechaFin)
      .subscribe({
        next: (archivoBlob: Blob) => {
          this.cargandoPDF = false;
          const urlArchivo = window.URL.createObjectURL(archivoBlob);
          window.open(urlArchivo, '_blank');
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.cargandoPDF = false;
          console.error("Error al generar el reporte", err);
          alert("Ocurrió un error al generar el reporte.");
          this.cdr.detectChanges();
        }
      });
  }
  
}