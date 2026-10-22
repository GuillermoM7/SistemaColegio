import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ConfiguracionService, AnioLectivo } from '../../services/configuracio.service';
import { GradosBaseComponent } from './grados-base/grados-base';
import { CarrerasComponent } from './carreras/carreras';

@Component({
  selector: 'app-configuracion-global',
  standalone: true,
  imports: [CommonModule, FormsModule, GradosBaseComponent, CarrerasComponent],
  templateUrl: './configuracion-global.html',
  styleUrl: './configuracion-global.css'
})
export class ConfiguracionGlobalComponent implements OnInit {
  
  pestanaActiva: number = 1;

  listaAnios: AnioLectivo[] = [];
  

  nuevoAnio: AnioLectivo = {
    anio: new Date().getFullYear(), 
    fechaInicio: '',
    fechaFin: '',
    activo: false
  };

  mensajeExito: string = '';
  mensajeError: string = '';

  constructor(private configService: ConfiguracionService, private cdr: ChangeDetectorRef) {}


  ngOnInit(): void {
    this.cargarAnios();
  }


  cambiarPestana(numeroPestana: number) {
    this.pestanaActiva = numeroPestana;
  }


  cargarAnios() {
    this.configService.obtenerAnios().subscribe({
      next: (datos) => {
        this.listaAnios = datos,
        this.cdr.detectChanges();
      },  
      error: (err) => console.error('Error al cargar años', err)
    });
  }


  guardarNuevoAnio() {
    if (!this.nuevoAnio.fechaInicio || !this.nuevoAnio.fechaFin || !this.nuevoAnio.anio) {
      this.mensajeError = 'Por favor complete todos los campos del año lectivo.';
      return;
    }
    this.configService.crearAnio(this.nuevoAnio).subscribe({
      next: (res) => {
        this.mensajeExito = res.mensaje || 'Año creado con éxito.';
        this.mensajeError = '';
        this.cargarAnios(); 
        this.nuevoAnio = { anio: new Date().getFullYear(), fechaInicio: '', fechaFin: '', activo: false };
      },
      error: (err) => {
        this.mensajeError = err.error?.mensaje || 'Error al crear el año lectivo.';
        this.mensajeExito = '';
      }
    });
  }


  activarAnioLectivo(idAnio: number) {
    if(confirm('¿Está seguro de activar este ciclo escolar? Todos los demás se desactivarán.')) {
      this.configService.activarAnio(idAnio).subscribe({
        next: (res) => {
          this.mensajeExito = res.mensaje || 'Año activado correctamente.';
          this.mensajeError = '';
          this.cargarAnios(); 
        },
        error: (err) => {
          this.mensajeError = err.error?.mensaje || 'Error al activar el año.';
          this.mensajeExito = '';
        }
      });
    }
  }
}