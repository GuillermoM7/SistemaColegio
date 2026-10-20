import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-paginacion',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './paginacion.html'
})
export class Paginacion {
  @Input() paginaActual: number = 1;
  @Input() totalPaginas: number = 1;
  @Input() totalRegistros: number = 0;
  
  @Output() cambioPagina = new EventEmitter<number>();

  cambiarPagina(nuevaPagina: number) {
    if (nuevaPagina >= 1 && nuevaPagina <= this.totalPaginas) {
      this.cambioPagina.emit(nuevaPagina);
    }
  }
}