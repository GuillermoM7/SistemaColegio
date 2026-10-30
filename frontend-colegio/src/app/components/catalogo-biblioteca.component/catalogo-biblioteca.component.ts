import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BibliotecaService } from '../../services/biblioteca.service';
import { Paginacion } from '../../compartido/paginacion/paginacion';
import { MisPrestamosComponent } from './mis-prestamos.component/mis-prestamos.component';
import { MisMultasComponent } from './mis-multas.component/mis-multas.component';
import { jwtDecode } from 'jwt-decode'; 

@Component({
  selector: 'app-catalogo-biblioteca',
  standalone: true,
  imports: [CommonModule, FormsModule, Paginacion, MisPrestamosComponent, MisMultasComponent],
  templateUrl: './catalogo-biblioteca.component.html'
})

export class CatalogoBibliotecaComponent implements OnInit {
  idUsuarioActual: number = 0;
  vistaActiva: 'catalogo' | 'pendientes' | 'multas' = 'catalogo';

  libros: any[] = [];
  librosFiltrados: any[] = [];
  terminoBusqueda: string = '';
  
  paginaActual: number = 1;
  itemsPorPagina: number = 15;
  totalPaginas: number = 0;


  constructor(private bibliotecaService: BibliotecaService, private cdr: ChangeDetectorRef) {}


  ngOnInit(): void {
    const usuarioGuardado = localStorage.getItem('usuarioLogueado');

    if (usuarioGuardado) {
      const datosBackend = JSON.parse(usuarioGuardado);
      try {
        const tokenDecodificado: any = jwtDecode(datosBackend.token);
        this.idUsuarioActual = tokenDecodificado.idUsuario; 
      } catch (error) {
        console.error('Error al leer el token en la biblioteca', error);
      }
    }
    this.cargarCatalogo();
  }


  cargarCatalogo() {
    this.bibliotecaService.obtenerLibros().subscribe(res => {
      this.libros = res;
      this.filtrarLibros();
    });
  }


  filtrarLibros() {
    if (this.terminoBusqueda.trim() === '') {
      this.librosFiltrados = this.libros;
    } else {
      const termino = this.terminoBusqueda.toLowerCase();
      this.librosFiltrados = this.libros.filter(l => 
        l.titulo.toLowerCase().includes(termino) || l.autor.toLowerCase().includes(termino)
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


  cambiarVista(vista: 'catalogo' | 'pendientes' | 'multas') {
    this.vistaActiva = vista;
  }
  
}