import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BibliotecaService } from '../../../services/biblioteca.service';
import { Paginacion } from '../../../compartido/paginacion/paginacion';

@Component({
  selector: 'app-mis-prestamos',
  standalone: true,
  imports: [CommonModule, Paginacion],
  templateUrl: './mis-prestamos.component.html'
})

export class MisPrestamosComponent implements OnInit {
  @Input() idUsuario!: number;
  @Output() onVolver = new EventEmitter<void>();

  prestamos: any[] = [];
  paginaActual: number = 1;
  totalPaginas: number = 0;


  constructor(private bibliotecaService: BibliotecaService, private cdr: ChangeDetectorRef) {}


  ngOnInit() {
    this.bibliotecaService.obtenerPrestamosUsuario(this.idUsuario).subscribe(res => {
      this.prestamos = res;
      this.totalPaginas = Math.ceil(this.prestamos.length / 15);
      this.cdr.detectChanges();
    });
  }


  get prestamosPaginados() {
    const inicio = (this.paginaActual - 1) * 15;
    return this.prestamos.slice(inicio, inicio + 15);
  }

}