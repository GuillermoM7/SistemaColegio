import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SecretariaEstudianteService } from '../../services/secretaria-estudiante.service';
import { DirectorAcademicoService } from '../../services/director-academico.service';
import { SecretariaInscripcionService } from '../../services/secretaria-inscripcion.service';

@Component({
  selector: 'app-realizar-inscripcion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './realizar-inscripcion.component.html'
})

export class RealizarInscripcionComponent implements OnInit {

  terminoBusqueda: string = '';
  resultadosBusqueda: any[] = [];
  estudianteSeleccionado: any = null;

  niveles: any[] = [];
  carreras: any[] = [];
  grados: any[] = [];
  secciones: any[] = [];

  nivelId: number = 0;
  carreraId: number = 0;
  gradoId: number = 0;
  seccionId: number = 0;
  esDiversificado: boolean = false;

  cargando: boolean = false;
  mensajeExito: string = '';
  mensajeError: string = '';


  constructor(
    private estudianteService: SecretariaEstudianteService,
    private directorService: DirectorAcademicoService,
    private inscripcionService: SecretariaInscripcionService,
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
    return this.estudianteSeleccionado !== null && this.seccionId > 0;
  }


  procesarInscripcion() {
    if (!this.formularioValido) return;
    this.cargando = true;
    this.mensajeError = '';
    const payload = {
      idEstudiante: this.estudianteSeleccionado.idEstudiante,
      idSeccion: Number(this.seccionId)
    };
    this.inscripcionService.inscribirEstudiante(payload).subscribe({
      next: (res) => {
        this.cargando = false;
        this.mensajeExito = res.mensaje;
        this.limpiarFormulario();
        this.cdr.detectChanges();       
        setTimeout(() => {
          this.mensajeExito = '';
          this.cdr.detectChanges();
        }, 6000);
      },
      error: (err) => {
        this.cargando = false;
        this.mensajeError = err.error?.mensaje || 'Ocurrió un error al procesar la inscripción.';
        this.cdr.detectChanges();
      }
    });
  }


  limpiarFormulario() {
    this.estudianteSeleccionado = null;
    this.nivelId = 0;
    this.carreraId = 0;
    this.gradoId = 0;
    this.seccionId = 0;
    this.esDiversificado = false;
    this.grados = [];
    this.secciones = [];
  }

}