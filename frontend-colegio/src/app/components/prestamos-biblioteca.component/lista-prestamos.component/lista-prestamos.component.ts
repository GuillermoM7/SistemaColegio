import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BibliotecaService } from '../../../services/biblioteca.service';

@Component({
  selector: 'app-lista-prestamos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-prestamos.component.html'
})

export class ListaPrestamosComponent implements OnInit {

  @Input() libro: any;
  @Output() onVolver = new EventEmitter<void>();

  prestamos: any[] = [];
  cargando: boolean = true;

  constructor(private bibliotecaService: BibliotecaService, private cdr: ChangeDetectorRef) {}

  
ngOnInit(): void {
    console.log("Pidiendo préstamos para el libro ID:", this.libro.idLibro);
    
    this.bibliotecaService.obtenerPrestamosActivos(this.libro.idLibro).subscribe({
      next: (res) => {
        console.log("Respuesta del servidor:", res);
        this.prestamos = res;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar préstamos', err);
        this.cargando = false;
      }
    });
  }

}