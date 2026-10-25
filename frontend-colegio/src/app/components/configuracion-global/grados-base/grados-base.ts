import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AcademicoService, Nivel, Grado, Carrera } from '../../../services/academico.service'; 

@Component({
  selector: 'app-grados-base',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './grados-base.html',
  styleUrl: './grados-base.css'
})
export class GradosBaseComponent implements OnInit {

  niveles: Nivel[] = [];
  gradosFiltrados: Grado[] = [];
  gradosVisibles: Grado[] = []; 

  carrerasActivas: Carrera[] = [];

  nivelSeleccionado: number = 0; 
  esDiversificado: boolean = false;

  mensajeExito: string = '';
  mensajeError: string = '';

  nuevoGrado: Grado = { nombre: '', idNivel: 0 };

  gradoEditandoId: number | null = null;
  gradoEditandoNombre: string = '';

  constructor(private academicoService: AcademicoService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.cargarNiveles();
    this.cargarCarrerasActivas();
  }


  cargarNiveles() {
    this.academicoService.obtenerNiveles().subscribe({
      next: (datos) => {
        this.niveles = datos;
        if (this.niveles.length > 0) {
          this.seleccionarNivel(this.niveles[0].idNivel);
        }
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error al cargar niveles', err)
    });
  }


  cargarCarrerasActivas() {
    this.academicoService.obtenerCarreras().subscribe({
      next: (datos) => {
        this.carrerasActivas = datos.filter(c => c.activa === true);
        this.cdr.detectChanges();
      }
    });
  }


  seleccionarNivel(idNivel: number) {
    this.nivelSeleccionado = idNivel;
    const nivelObj = this.niveles.find(n => n.idNivel === idNivel);
    this.esDiversificado = nivelObj ? nivelObj.nombre.toLowerCase().includes('diversificado') : false;
    
    if (!this.esDiversificado) {
      this.nuevoGrado.idCarrera = undefined; 
    }
    this.academicoService.obtenerGradosPorNivel(idNivel).subscribe({
      next: (datos) =>{ 
        this.gradosFiltrados = datos;
        this.aplicarFiltroCarrera();
      },
      error: (err) => console.error('Error al filtrar grados', err)
    });
  }


  aplicarFiltroCarrera() {
    if (this.esDiversificado && this.nuevoGrado.idCarrera) {
      this.gradosVisibles = this.gradosFiltrados.filter(g => g.idCarrera === Number(this.nuevoGrado.idCarrera));
    } else {
      this.gradosVisibles = [...this.gradosFiltrados];
    }
    this.cdr.detectChanges();
  }


  guardarGrado() {
    if (!this.nuevoGrado.nombre.trim()) {
      this.mensajeError = 'Ingrese el nombre del grado.';
      return;
    }   
    this.nuevoGrado.idNivel = this.nivelSeleccionado;
    this.academicoService.crearGrado(this.nuevoGrado).subscribe({
      next: (res) => {
        this.mensajeExito = res.mensaje || 'Grado creado exitosamente.';
        this.mensajeError = '';
        this.nuevoGrado.nombre = ''; 
        this.seleccionarNivel(this.nivelSeleccionado); 
      },
      error: (err) => {
        this.mensajeError = err.error?.mensaje || 'Error al crear el grado.';
        this.mensajeExito = '';
        this.cdr.detectChanges();
      }
    });
  }


  iniciarEdicion(grado: Grado) {
    this.gradoEditandoId = grado.idGrado!;
    this.gradoEditandoNombre = grado.nombre;
  }


  cancelarEdicion() {
    this.gradoEditandoId = null;
    this.gradoEditandoNombre = '';
  }


  guardarEdicion(gradoOriginal: any) {
    if (!this.gradoEditandoNombre.trim()) {
      this.mensajeError = 'El nombre del grado no puede estar vacío.';
      return;
    }
    const payloadLimpio = {
      idGrado: gradoOriginal.idGrado,
      nombre: this.gradoEditandoNombre,
      idNivel: gradoOriginal.idNivel,
      idCarrera: gradoOriginal.idCarrera
    };
    this.academicoService.actualizarGrado(payloadLimpio).subscribe({
      next: () => {
        this.mensajeExito = 'Nombre del grado actualizado correctamente.';
        this.cancelarEdicion();
        this.seleccionarNivel(this.nivelSeleccionado); 
      },
      error: (err) => {
        this.mensajeError = err.error?.mensaje || 'Error al actualizar el grado.';
        this.cdr.detectChanges();
      }
    });
  }

}