import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule, formatDate } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BibliotecaService } from '../../../services/biblioteca.service';

@Component({
  selector: 'app-formulario-prestamo',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './formulario-prestamo.component.html'
})

export class FormularioPrestamoComponent implements OnInit {

  @Input() libro: any;
  @Output() onCerrar = new EventEmitter<boolean>(); // Emite true si se debe recargar el inventario

  terminoBusqueda: string = '';
  usuariosEncontrados: any[] = [];
  usuarioSeleccionado: any = null;
  fechaEsperada: string = '';
  
  buscando: boolean = false;
  guardando: boolean = false;

  
  constructor(private bibliotecaService: BibliotecaService) {}


  //poner 7 diasa de diferencia desde el inicio
  ngOnInit(): void {
    const fecha = new Date();
    fecha.setDate(fecha.getDate() + 7);
    this.fechaEsperada = formatDate(fecha, 'yyyy-MM-dd', 'en-US');
  }


  buscarUsuario() {
    if (this.terminoBusqueda.trim().length < 3) {
      this.usuariosEncontrados = [];
      return;
    }    
    this.buscando = true;
    this.bibliotecaService.buscarUsuariosPrestamo(this.terminoBusqueda).subscribe({
      next: (res) => {
        this.usuariosEncontrados = res;
        this.buscando = false;
      },
      error: () => this.buscando = false
    });
  }


  seleccionarUsuario(usuario: any) {
    this.usuarioSeleccionado = usuario;
    this.terminoBusqueda = '';
    this.usuariosEncontrados = [];
  }


  guardarPrestamo() {
    if (!this.usuarioSeleccionado || !this.fechaEsperada) return;
    this.guardando = true;

    const payload = {
      idUsuario: this.usuarioSeleccionado.idUsuario,
      fechaEsperada: this.fechaEsperada
    };
    this.bibliotecaService.registrarPrestamo(this.libro.idLibro, payload).subscribe({
      next: () => {
        this.guardando = false;
        this.onCerrar.emit(true); // Cierra y recarga la tabla para ver el inventario restado
      },
      error: (err) => {
        alert('Error al registrar préstamo. Revise la disponibilidad.');
        this.guardando = false;
      }
    });
  }
  
}