import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DirectorCursoService } from '../../services/director-curso.service';
import { Paginacion } from '../../compartido/paginacion/paginacion';
import { CrearCursoComponent } from './crear-curso.component/crear-curso.component';

@Component({
  selector: 'app-director-cursos',
  standalone: true,
  imports: [CommonModule, FormsModule, Paginacion, CrearCursoComponent],
  templateUrl: './director-cursos.component.html'
})
export class DirectorCursosComponent implements OnInit {
  listaCursos: any[] = [];
  paginaActual: number = 1;
  totalPaginas: number = 1;
  totalRegistros: number = 0;
  
  terminoBusqueda: string = '';
  mostrarModal: boolean = false;
  mensajeExito: string = '';

  constructor(
    private cursoService: DirectorCursoService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    this.cargarPagina();
  }


  cargarPagina() {
    this.cursoService.listarPaginados(this.paginaActual, 5, this.terminoBusqueda).subscribe({
      next: (res) => {
        this.listaCursos = res.cursos || [];
        this.totalRegistros = res.totalRegistros || 0;
        this.totalPaginas = res.totalPaginas || 0;
        this.cdr.detectChanges();
      }
    });
  }


  buscar() {
    this.paginaActual = 1; 
    this.cargarPagina();
  }


  limpiarBusqueda() {
    this.terminoBusqueda = '';
    this.buscar();
  }


  alCambiarPagina(nuevaPagina: number) {
    this.paginaActual = nuevaPagina;
    this.cargarPagina();
  }


  abrirModal() { 
    this.mostrarModal = true; 
  }


  cerrarModal() {
    this.mostrarModal = false; 
    }


  refrescarTrasGuardar() {
    this.mensajeExito = 'Curso creado exitosamente.';
    this.cerrarModal();
    this.cargarPagina();    
    setTimeout(() => {
      this.mensajeExito = '';
      this.cdr.detectChanges();
    }, 3000);
  }
  
}