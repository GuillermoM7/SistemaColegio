import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReportesService } from '../../services/reportes.service';

@Component({
  selector: 'app-reporte-ingresos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte-ingresos.component.html'
})

export class ReporteIngresosComponent {

  fechaInicio: string = '';
  fechaFin: string = '';
  cargandoPDF: boolean = false;


  constructor(
    private reportesService: ReportesService,
    private cdr: ChangeDetectorRef
  ) {}

  
  generarPDF() {
    this.cargandoPDF = true;
    
    this.reportesService.obtenerReporteIngresos(this.fechaInicio, this.fechaFin)
      .subscribe({
        next: (archivoBlob: Blob) => {
          this.cargandoPDF = false;
          const urlArchivo = window.URL.createObjectURL(archivoBlob);
          window.open(urlArchivo, '_blank');
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.cargandoPDF = false;
          console.error("Error al generar el reporte de ingresos", err);
          alert("Ocurrió un error al generar el reporte.");
          this.cdr.detectChanges();
        }
      });
  }
}