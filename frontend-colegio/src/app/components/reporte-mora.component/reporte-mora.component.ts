import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportesService } from '../../services/reportes.service';

@Component({
  selector: 'app-reporte-mora',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reporte-mora.component.html'
})
export class ReporteMoraComponent {

  cargandoPDF: boolean = false;

  constructor(
    private reportesService: ReportesService,
    private cdr: ChangeDetectorRef
  ) {}

  generarPDF() {
    this.cargandoPDF = true;
    
    this.reportesService.obtenerReporteMoraBiblioteca()
      .subscribe({
        next: (archivoBlob: Blob) => {
          this.cargandoPDF = false;
          const urlArchivo = window.URL.createObjectURL(archivoBlob);
          window.open(urlArchivo, '_blank');
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.cargandoPDF = false;
          console.error("Error al generar el reporte de mora", err);
          alert("Ocurrió un error al generar el reporte.");
          this.cdr.detectChanges();
        }
      });
  }
}