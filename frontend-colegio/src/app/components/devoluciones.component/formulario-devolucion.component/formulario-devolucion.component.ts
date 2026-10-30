import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BibliotecaService } from '../../../services/biblioteca.service';

@Component({
  selector: 'app-formulario-devolucion',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './formulario-devolucion.component.html'
})
export class FormularioDevolucionComponent implements OnInit {
  @Input() prestamo: any;
  @Output() onCerrar = new EventEmitter<boolean>();
  
  guardando: boolean = false;

  constructor(private bibliotecaService: BibliotecaService) {}

  ngOnInit() {}


  recogerLibro() {
    this.guardando = true;
    const payload = {
      idPrestamo: this.prestamo.idPrestamo,
      idLibro: this.prestamo.idLibro,
      montoMulta: this.prestamo.montoMulta 
    };

    this.bibliotecaService.procesarDevolucion(payload).subscribe({
      next: () => this.onCerrar.emit(true),
      error: () => { alert('Error de conexión'); this.guardando = false; }
    });
  }

}