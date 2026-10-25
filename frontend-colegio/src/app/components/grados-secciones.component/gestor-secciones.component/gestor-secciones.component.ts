import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DirectorAcademicoService } from '../../../services/director-academico.service';
import { CrearSeccionComponent } from '../crear-seccion.component/crear-seccion.component';

@Component({
  selector: 'app-gestor-secciones',
  standalone: true,
  imports: [CommonModule, CrearSeccionComponent],
  templateUrl: './gestor-secciones.component.html'
})
export class GestorSeccionesComponent implements OnInit {

  @Input() gradoEnFoco: any;
  @Output() onVolver = new EventEmitter<void>();

  secciones: any[] = [];
  creandoNuevaSeccion: boolean = false; 


  constructor(
    private directorService: DirectorAcademicoService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    this.cargarSecciones();
  }


  cargarSecciones() {
    if (this.gradoEnFoco) {
      this.directorService.obtenerSeccionesPorGrado(this.gradoEnFoco.idGrado).subscribe({
        next: (res) => {
          this.secciones = res;
          this.cdr.detectChanges();
        }
      });
    }
  }


  abrirCreacionSeccion() {
    this.creandoNuevaSeccion = true;
  }


  cerrarCreacionSeccion(recargar: boolean = false) {
    this.creandoNuevaSeccion = false;
    if (recargar) {
      this.cargarSecciones();
    }
  }
  
}