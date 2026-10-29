import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { MaestroService } from '../../services/maestro.service';
import { PanelClaseComponent } from './panel-clase.component/panel-clase.component';

@Component({
  selector: 'app-dashboard-maestro',
  standalone: true,
  imports: [CommonModule, PanelClaseComponent],
  templateUrl: './gestion-maestro.component.html'
})

export class GestionMaestroComponent implements OnInit {
  
  nombreMaestro: string = '';
  idEmpleadoLogueado: number = 0;
  cursosAsignados: any[] = [];
  cargando: boolean = true;
  vistaActual: number = 1;
  cursoEnFoco: any = null;


  constructor(
    private maestroService: MaestroService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    const usuarioGuardado = localStorage.getItem('usuarioLogueado');
    if (usuarioGuardado) {
      try {
        const datosBackend = JSON.parse(usuarioGuardado);
        this.nombreMaestro = datosBackend.nombre;
        
        const tokenDecodificado: any = jwtDecode(datosBackend.token);
        this.idEmpleadoLogueado = tokenDecodificado.idUsuario; 
        
        this.cargarCursos();
      } catch (error) {
        console.error('Error al decodificar token', error);
        this.router.navigate(['/login']);
      }
    } else {
      this.router.navigate(['/login']);
    }
  }


  cargarCursos() {
    this.maestroService.obtenerCursosAsignados(this.idEmpleadoLogueado).subscribe({
      next: (res) => {
        this.cursosAsignados = res;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar cursos', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  
  abrirClase(curso: any) {
    this.cursoEnFoco = curso;
    this.vistaActual = 2;
    this.cdr.detectChanges();
  }


  volverAlDashboard() {
    this.cursoEnFoco = null;
    this.vistaActual = 1;
    this.cdr.detectChanges();
  }

}