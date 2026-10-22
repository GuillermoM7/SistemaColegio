import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AcademicoService, Carrera } from '../../../services/academico.service';

@Component({
  selector: 'app-carreras',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './carreras.html',
  styleUrl: './carreras.css'
})
export class CarrerasComponent implements OnInit {

  listaCarreras: Carrera[] = [];
  mensajeExito: string = '';
  mensajeError: string = '';

  nuevaCarrera: Carrera = { idCarrera: 0, nombre: '', activa: true };

  constructor(private academicoService: AcademicoService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.cargarCarreras();
  }

  cargarCarreras() {
    this.academicoService.obtenerCarreras().subscribe({
      next: (datos) => {
        this.listaCarreras = datos,
        this.cdr.detectChanges();
      },  
      error: (err) => {
        console.error('Error al cargar carreras', err);
        this.mensajeError = 'No se pudieron cargar las carreras.';
      }
    });
  }

  cambiarEstado(carrera: Carrera) {
    const nuevoEstado = !carrera.activa;
    const accion = nuevoEstado ? 'ACTIVAR' : 'DESACTIVAR';
    
    if (confirm(`¿Está seguro que desea ${accion} la carrera: ${carrera.nombre}?`)) {
      this.academicoService.cambiarEstadoCarrera(carrera.idCarrera!, nuevoEstado).subscribe({
        next: (res) => {
          this.mensajeExito = res.mensaje || `Carrera actualizada con éxito.`;
          this.mensajeError = '';
          this.cargarCarreras();
        },
        error: (err) => {
          console.error(err);
          this.mensajeError = 'Hubo un error al cambiar el estado de la carrera.';
          this.mensajeExito = '';
        }
      });
    }
  }

  guardarCarrera() {
    if (!this.nuevaCarrera.nombre.trim()) {
      this.nuevaCarrera = { idCarrera: 0, nombre: '', activa: true };
      this.mensajeError = 'Por favor, ingrese el nombre de la carrera.';
      return;
    }

    this.academicoService.crearCarrera(this.nuevaCarrera).subscribe({
      next: (res) => {
        this.mensajeExito = res.mensaje || 'Carrera creada exitosamente.';
        this.mensajeError = '';
        this.nuevaCarrera.nombre = ''; 
        this.cargarCarreras(); 
      },
      error: (err) => {
        this.mensajeError = err.error?.mensaje || 'Error al crear la carrera.';
        this.mensajeExito = '';
      }
    });
  }

}