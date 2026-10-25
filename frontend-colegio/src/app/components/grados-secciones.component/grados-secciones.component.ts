import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DirectorAcademicoService } from '../../services/director-academico.service';
import { AsignarCursosComponent } from './asignar-cursos.component/asignar-cursos.component';
import { GestorSeccionesComponent } from './gestor-secciones.component/gestor-secciones.component';

@Component({
  selector: 'app-grados-secciones',
  standalone: true,
  imports: [CommonModule, FormsModule, AsignarCursosComponent, GestorSeccionesComponent],
  templateUrl: './grados-secciones.component.html'
})
export class GradosSeccionesComponent implements OnInit {

  niveles: any[] = [];
  carreras: any[] = [];
  grados: any[] = [];

  nivelSeleccionado: number = 0;
  carreraSeleccionada: number = 0;
  esDiversificado: boolean = false;
  mensajeExitoGlobal: string = '';

  vistaActual: number = 1; 
  gradoEnFoco: any = null; 

  constructor(
    private directorService: DirectorAcademicoService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    this.cargarCatalogos();
  }


  cargarCatalogos() {
    this.directorService.obtenerNiveles().subscribe(res => {
      this.niveles = res;
      this.cdr.detectChanges();
    });
    this.directorService.obtenerCarrerasActivas().subscribe(res => {
      this.carreras = res.filter((c: any) => c.activa === true);
      this.cdr.detectChanges();
    });
  }


  alCambiarNivel() {
    const nivelObj = this.niveles.find(n => n.idNivel === Number(this.nivelSeleccionado));
    this.esDiversificado = nivelObj ? nivelObj.nombre.toLowerCase().includes('diversificado') : false;
    
    this.carreraSeleccionada = 0;
    this.grados = [];
    
    if (!this.esDiversificado) {
      this.buscarGrados();
    }
    this.cdr.detectChanges();
  }


  buscarGrados() {
    if (this.esDiversificado && !this.carreraSeleccionada) {
      this.grados = [];
      return;
    }
    const idCarreraParam = this.esDiversificado ? this.carreraSeleccionada : undefined;
    this.directorService.obtenerGradosConCurriculo(this.nivelSeleccionado, idCarreraParam).subscribe({
      next: (res) => {
        this.grados = res;
        this.cdr.detectChanges();
      }
    });
  }


  abrirAsignacionCursos(grado: any) {
    this.gradoEnFoco = grado;
    this.vistaActual = 2;
  }


  abrirSecciones(grado: any) {
    this.gradoEnFoco = grado;
    this.vistaActual = 3;
  }


  volverVistaPrincipal(mensajeExito?: string | void) {
    this.vistaActual = 1;
    this.gradoEnFoco = null;
    if (typeof mensajeExito === 'string') {
      this.mensajeExitoGlobal = mensajeExito;
      setTimeout(() => {
        this.mensajeExitoGlobal = '';
        this.cdr.detectChanges();
      }, 5000);
    }
    
    this.buscarGrados(); 
  }
  
}