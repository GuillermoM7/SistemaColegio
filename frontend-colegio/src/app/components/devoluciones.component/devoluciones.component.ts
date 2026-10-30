import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BibliotecaService } from '../../services/biblioteca.service';
import { Paginacion } from '../../compartido/paginacion/paginacion';
import { FormularioDevolucionComponent } from './formulario-devolucion.component/formulario-devolucion.component';

@Component({
  selector: 'app-devoluciones',
  standalone: true,
  imports: [CommonModule, FormsModule, Paginacion, FormularioDevolucionComponent],
  templateUrl: './devoluciones.component.html'
})

export class DevolucionesComponent {
  terminoBusqueda: string = '';
  usuariosEncontrados: any[] = [];
  usuarioSeleccionado: any = null;
  
  prestamosUsuario: any[] = [];
  prestamosPaginados: any[] = [];
  paginaActual: number = 1;
  totalPaginas: number = 0;
  
  prestamoEnDevolucion: any = null;


  constructor(private bibliotecaService: BibliotecaService, private cdr: ChangeDetectorRef) {}


  buscarUsuario() {
    if (this.terminoBusqueda.trim().length < 3) {
      this.usuariosEncontrados = [];
      return;
    }
    this.bibliotecaService.buscarUsuariosPrestamo(this.terminoBusqueda).subscribe(res => {
      this.usuariosEncontrados = res;
      this.cdr.detectChanges();
    });
  }


  seleccionarUsuario(usuario: any) {
    this.usuarioSeleccionado = usuario;
    this.usuariosEncontrados = [];
    this.terminoBusqueda = '';
    this.cargarPrestamos();
  }


  cargarPrestamos() {
    this.bibliotecaService.obtenerPrestamosUsuario(this.usuarioSeleccionado.idUsuario).subscribe(res => {
      this.prestamosUsuario = res;
      this.totalPaginas = Math.ceil(this.prestamosUsuario.length / 15);
      this.onCambioPagina(1);
    });
  }


  onCambioPagina(pag: number) {
    this.paginaActual = pag;
    const inicio = (pag - 1) * 15;
    this.prestamosPaginados = this.prestamosUsuario.slice(inicio, inicio + 15);
    this.cdr.detectChanges();
  }


  abrirDevolucion(prestamo: any) {
    this.prestamoEnDevolucion = prestamo;
  }


  cerrarFormulario(recargar: boolean) {
    this.prestamoEnDevolucion = null;
    if (recargar) this.cargarPrestamos();
  }

}