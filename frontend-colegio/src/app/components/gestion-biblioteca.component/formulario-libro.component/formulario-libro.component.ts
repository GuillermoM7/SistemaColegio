import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BibliotecaService } from '../../../services/biblioteca.service';

@Component({
  selector: 'app-formulario-libro',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './formulario-libro.component.html'
})

export class FormularioLibroComponent implements OnInit {

  @Input() libroEditar: any = null; // Si es null, es modo Crear
  @Output() onCerrar = new EventEmitter<boolean>(); // true si guardó algo y hay que recargar

  libroForm: any = { titulo: '', autor: '', editorial: '', cantidadTotal: 1 };
  esEdicion: boolean = false;
  permiteEditarCantidad: boolean = true;
  guardando: boolean = false;

  constructor(private bibliotecaService: BibliotecaService) {}

  ngOnInit(): void {
    if (this.libroEditar) {
      this.esEdicion = true;
      this.libroForm = { ...this.libroEditar }; // Cargar datos previos
      if (this.libroForm.cantidadDisponible < this.libroForm.cantidadTotal) {
        this.permiteEditarCantidad = false;
      }
    }
  }


  guardar() {
    this.guardando = true;   
    // Si es un libro nuevo, la cantidad disponible inicial es igual a la cantidad total ingresada
    if (!this.esEdicion) {
      this.libroForm.cantidadDisponible = this.libroForm.cantidadTotal;
    } else if (this.esEdicion && this.permiteEditarCantidad) {
      this.libroForm.cantidadDisponible = this.libroForm.cantidadTotal;
    }
    const operacion = this.esEdicion 
      ? this.bibliotecaService.actualizarLibro(this.libroForm.idLibro, this.libroForm)
      : this.bibliotecaService.registrarLibro(this.libroForm);
    operacion.subscribe({
      next: () => this.onCerrar.emit(true), // Aviso al padre para recargar
      error: () => { alert('Error al guardar'); this.guardando = false; }
    });
  }

}