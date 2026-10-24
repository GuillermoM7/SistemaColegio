import { Component, OnInit, Output, EventEmitter, ChangeDetectorRef  } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DirectorAlumnoService } from '../../../services/director-alumno.service'; 

@Component({
  selector: 'app-registro-usuario-alumno',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './registro-usuario-alumno.component.html'
})
export class RegistroUsuarioAlumnoComponent implements OnInit {
  @Output() onCancelar = new EventEmitter<void>();
  @Output() onGuardado = new EventEmitter<void>();

  estudiantesHuerfanos: any[] = [];
  mensajeError: string = '';

  nuevoUsuario: any = {
    idEstudiante: 0,
    correo: '',
    password: ''
  };

  constructor(
    private alumnoService: DirectorAlumnoService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    this.cargarHuerfanos();
  }


  cargarHuerfanos() {
    this.alumnoService.listarSinUsuario().subscribe({
      next: (res) => {
        this.estudiantesHuerfanos = res || [];
        this.cdr.detectChanges();
      },
      error: () => this.mensajeError = 'Error al cargar la lista de estudiantes pendientes.'
    });
  }


  guardar() {
    if (this.nuevoUsuario.idEstudiante === 0) {
      this.mensajeError = 'Debe seleccionar un estudiante de la lista.';
      return;
    }
    this.alumnoService.crearUsuario(this.nuevoUsuario).subscribe({
      next: () => this.onGuardado.emit(),
      error: (err) => this.mensajeError = err.error?.mensaje || 'Error al asignar el usuario.'
    });
  }

}