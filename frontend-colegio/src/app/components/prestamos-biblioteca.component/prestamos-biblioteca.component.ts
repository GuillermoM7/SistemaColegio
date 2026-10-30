import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BibliotecaService } from '../../services/biblioteca.service';
import { Paginacion } from '../../compartido/paginacion/paginacion';
import { ListaPrestamosComponent } from './lista-prestamos.component/lista-prestamos.component';
import { FormularioPrestamoComponent } from './formulario-prestamo.component/formulario-prestamo.component';

@Component({
  selector: 'app-prestamos-biblioteca',
  standalone: true,
  imports: [CommonModule, FormsModule, Paginacion, ListaPrestamosComponent, FormularioPrestamoComponent], 
  templateUrl: './prestamos-biblioteca.component.html'
})

export class PrestamosBibliotecaComponent implements OnInit {
  libros: any[] = [];
  librosFiltrados: any[] = [];
  cargando: boolean = true;

  terminoBusqueda: string = '';
  paginaActual: number = 1;
  itemsPorPagina: number = 15;
  totalPaginas: number = 0;

  // Variables para controlar las vistas hijas
  libroSeleccionado: any = null;
  vistaActiva: 'tabla' | 'lista-prestamos' | 'formulario-prestamo' = 'tabla';

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
        this.filtrarLibros();
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => { console.error('Error al cargar libros', err); this.cargando = false; }
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


  // Métodos de navegación
  abrirListaPrestamos(libro: any) {
    this.libroSeleccionado = libro;
    this.vistaActiva = 'lista-prestamos';
  }

  abrirFormularioPrestamo(libro: any) {
    this.libroSeleccionado = libro;
    this.vistaActiva = 'formulario-prestamo';
  }

  cerrarVistasHijas(recargar: boolean = false) {
    this.vistaActiva = 'tabla';
    this.libroSeleccionado = null;
    if (recargar) this.cargarLibros(); 
  }
}