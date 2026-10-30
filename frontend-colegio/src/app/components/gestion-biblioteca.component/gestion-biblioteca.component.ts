import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BibliotecaService } from '../../services/biblioteca.service';
import { Paginacion } from '../../compartido/paginacion/paginacion';
import { FormularioLibroComponent } from './formulario-libro.component/formulario-libro.component';

@Component({
  selector: 'app-gestion-biblioteca',
  standalone: true,
  imports: [CommonModule, FormsModule, Paginacion, FormularioLibroComponent],
  templateUrl: './gestion-biblioteca.component.html'
})

export class GestionBibliotecaComponent implements OnInit {
  libros: any[] = [];
  librosFiltrados: any[] = [];
  cargando: boolean = true;

  // Búsqueda y Paginación
  terminoBusqueda: string = '';
  paginaActual: number = 1;
  itemsPorPagina: number = 15; 
  totalPaginas: number = 0;

  // Control del modal/formulario
  mostrarFormulario: boolean = false;
  libroEnEdicion: any = null;


  constructor(
    private bibliotecaService: BibliotecaService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    this.cargarLibros();
  }


  cargarLibros() {
    this.cargando = true;
    this.bibliotecaService.obtenerLibros().subscribe({
      next: (res) => {
        this.libros = res;
        this.filtrarLibros(); // Aplica filtro y paginación
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => { console.error('Error', err); this.cargando = false; }
    });
  }


  filtrarLibros() {
    if (this.terminoBusqueda.trim() === '') {
      this.librosFiltrados = this.libros;
    } else {
      const termino = this.terminoBusqueda.toLowerCase();
      this.librosFiltrados = this.libros.filter(l => 
        l.titulo.toLowerCase().includes(termino) || 
        l.autor.toLowerCase().includes(termino)
      );
    }
    this.totalPaginas = Math.ceil(this.librosFiltrados.length / this.itemsPorPagina);
    this.paginaActual = 1; 
    this.cdr.detectChanges();
  }


  get librosPaginados(): any[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    return this.librosFiltrados.slice(inicio, inicio + this.itemsPorPagina);
  }


  onCambioPagina(pagina: number) {
    this.paginaActual = pagina;
    this.cdr.detectChanges();
  }


  // Métodos para nuevo y editar
  abrirParaNuevo() {
    this.libroEnEdicion = null;
    this.mostrarFormulario = true;
  }

  abrirParaEditar(libro: any) {
    this.libroEnEdicion = { ...libro };
    this.mostrarFormulario = true;
  }
  // ------

  
  cerrarFormulario(recargar: boolean) {
    this.mostrarFormulario = false;
    this.libroEnEdicion = null;
    if (recargar) this.cargarLibros();
  }


  eliminarLibro(id: number) {
    if(confirm('¿Está seguro de eliminar este libro del inventario?')) {
      this.bibliotecaService.eliminarLibro(id).subscribe({
        next: () => this.cargarLibros(),
        error: () => alert('Error al eliminar. Verifique que no tenga préstamos activos.')
      });
    }
  }

}