import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DirectorAcademicoService } from '../../services/director-academico.service';
import { ReportesService } from '../../services/reportes.service';

@Component({
  selector: 'app-reporte-solvencia',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte-solvencia.component.html'
})

export class ReporteSolvenciaComponent implements OnInit {

  niveles: any[] = [];
  carreras: any[] = [];
  grados: any[] = [];
  secciones: any[] = [];

  nivelId: number = 0;
  carreraId: number = 0;
  gradoId: number = 0;
  seccionId: number = 0;
  
  esDiversificado: boolean = false;
  cargandoPDF: boolean = false;


  constructor(
    private directorService: DirectorAcademicoService,
    private reportesService: ReportesService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    this.cargarCatalogosBase();
  }


  cargarCatalogosBase() {
    this.directorService.obtenerNiveles().subscribe(res => this.niveles = res);
    this.directorService.obtenerCarrerasActivas().subscribe(res => {
      this.carreras = res.filter((c: any) => c.activa === true);
    });
  }


  alCambiarNivel() {
    const nivelObj = this.niveles.find(n => n.idNivel === Number(this.nivelId));
    this.esDiversificado = nivelObj ? nivelObj.nombre.toLowerCase().includes('diversificado') : false;   
    
    this.carreraId = 0;
    this.gradoId = 0;
    this.seccionId = 0;
    this.grados = [];
    this.secciones = [];
    
    if (!this.esDiversificado) {
      this.buscarGrados();
    }
    this.cdr.detectChanges();
  }


  buscarGrados() {
    if (this.esDiversificado && !this.carreraId) return;
    const idCarreraParam = this.esDiversificado ? this.carreraId : undefined;
    
    this.directorService.obtenerGradosConCurriculo(this.nivelId, idCarreraParam).subscribe(res => {
      this.grados = res.filter((g: any) => g.tieneCurriculoActivo);
      this.gradoId = 0;
      this.secciones = [];
      this.cdr.detectChanges();
    });
  }


  alCambiarGrado() {
    this.seccionId = 0;
    this.directorService.obtenerSeccionesPorGrado(this.gradoId).subscribe(res => {
      this.secciones = res;
      this.cdr.detectChanges();
    });
  }


  get formularioValido(): boolean {
    return this.seccionId > 0;
  }

  
  generarPDF() {
    if (!this.formularioValido) return;
    
    this.cargandoPDF = true;
    this.reportesService.obtenerReporteSolvencia(this.gradoId, this.seccionId)
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