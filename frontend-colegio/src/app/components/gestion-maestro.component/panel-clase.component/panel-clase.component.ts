import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaestroService } from '../../../services/maestro.service';
import { Paginacion } from '../../../compartido/paginacion/paginacion';
import { AsistenciaClaseComponent } from '../asistencia-clase.component/asistencia-clase.component';
import { NotasClaseComponent } from '../notas-clase.component/notas-clase.component';

@Component({
  selector: 'app-panel-clase',
  standalone: true,
  imports: [CommonModule, Paginacion, AsistenciaClaseComponent, NotasClaseComponent],
  templateUrl: './panel-clase.component.html'
})

export class PanelClaseComponent implements OnInit {
  @Input() cursoActual: any;
  @Output() onVolver = new EventEmitter<void>();

  nominaAlumnos: any[] = [];
  cargando: boolean = true;
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 0;
  pestanaActiva: string = 'nomina';


  constructor(
    private maestroService: MaestroService,
    private cdr: ChangeDetectorRef
  ) {}

  
  ngOnInit(): void {
    this.maestroService.obtenerNominaAlumnos(this.cursoActual.idAsignacion).subscribe({
      next: (res) => {
        this.nominaAlumnos = res;
        this.totalPaginas = Math.ceil(this.nominaAlumnos.length / this.itemsPorPagina);
        
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar nómina', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }


  get nominaPaginada(): any[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    return this.nominaAlumnos.slice(inicio, fin);
  }

  
  onCambioPagina(nuevaPagina: number) {
    this.paginaActual = nuevaPagina;
    this.cdr.detectChanges();
  }

  
  cambiarPestana(pestana: string) {
    this.pestanaActiva = pestana;
    this.cdr.detectChanges();
  }
}