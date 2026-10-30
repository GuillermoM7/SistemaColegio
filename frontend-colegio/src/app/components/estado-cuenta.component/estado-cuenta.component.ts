import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { EstudianteOperativoService } from '../../services/estudiante-operativo.service';

@Component({
  selector: 'app-estado-cuenta',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './estado-cuenta.component.html'
})

export class EstadoCuentaComponent implements OnInit {
  resumenFinanciero: any = null;
  cargando: boolean = true;
  errorAutenticacion: boolean = false;


  constructor(
    private estudianteOperativoService: EstudianteOperativoService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    const usuarioGuardado = localStorage.getItem('usuarioLogueado');
    if (usuarioGuardado) {
      const datosBackend = JSON.parse(usuarioGuardado);
      try {
        const tokenDecodificado: any = jwtDecode(datosBackend.token);
        this.cargarEstadoCuenta(tokenDecodificado.idUsuario);
      } catch (error) {
        this.router.navigate(['/login']);
      }
    } else {
      this.router.navigate(['/login']);
    }
  }


  cargarEstadoCuenta(idUsuario: number) {
    this.estudianteOperativoService.obtenerEstadoCuenta(idUsuario).subscribe({
      next: (res) => {
        this.resumenFinanciero = res;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar estado de cuenta', err);
        this.errorAutenticacion = true;
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  
  obtenerNombreMes(mes: number): string {
    if (!mes) return '';
    const meses = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    return meses[mes - 1] || '';
  }
}