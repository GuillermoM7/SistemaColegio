import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BibliotecaService } from '../../../services/biblioteca.service';
import { Paginacion } from '../../../compartido/paginacion/paginacion';

@Component({
  selector: 'app-mis-multas',
  standalone: true,
  imports: [CommonModule, Paginacion],
  templateUrl: './mis-multas.component.html'
})

export class MisMultasComponent implements OnInit {
  @Input() idUsuario!: number;
  @Output() onVolver = new EventEmitter<void>();

  multas: any[] = [];
  paginaActual: number = 1;
  totalPaginas: number = 0;


  constructor(private bibliotecaService: BibliotecaService, private cdr: ChangeDetectorRef) {}


  ngOnInit() {
    this.bibliotecaService.obtenerMultasUsuario(this.idUsuario).subscribe({
      next: (res) => {
        this.multas = res;
        this.totalPaginas = Math.ceil(this.multas.length / 15);
        this.cdr.detectChanges(); 
      },
      error: (err) => console.error('Error al cargar multas', err)
    });
  }


  get multasPaginadas() {
    const inicio = (this.paginaActual - 1) * 15;
    return this.multas.slice(inicio, inicio + 15);
  }
  
}