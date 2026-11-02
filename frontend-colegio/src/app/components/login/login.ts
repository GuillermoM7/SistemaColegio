import { Component, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common'; 
import { AuthService } from '../../services/autenticacion'; 
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule], 
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {

  vistaActiva = 0; 
  
  correo = '';
  password = '';
  
  correoRecuperacion = '';
  nuevaPassword = '';
  confirmarPassword = '';
  
  mensajeError = '';
  mensajeExito = '';
  procesando = false;


  constructor(private authService: AuthService, private router: Router, private cdr: ChangeDetectorRef) {}


  ejecutarLogin() {
    this.procesando = true;
    this.authService.login(this.correo, this.password).subscribe({
      next: (res) => {
        localStorage.setItem('usuarioLogueado', JSON.stringify(res));
        this.router.navigate(['/pantalla-principal']);
      },
      error: () => {
        this.mensajeError = 'Credenciales incorrectas.';
        this.procesando = false;
        this.cdr.detectChanges();
      }
    });
  }


  verificarCorreo() {
    if (!this.correoRecuperacion) return;
    this.procesando = true;
    this.mensajeError = '';
    
    this.authService.verificarCorreo(this.correoRecuperacion).subscribe({
      next: () => {
        this.vistaActiva = 2; 
        this.procesando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.mensajeError = 'El correo no está registrado o está inactivo.';
        this.procesando = false;
        this.cdr.detectChanges();
      }
    });
  }


  guardarNuevaPassword() {
    if (this.nuevaPassword !== this.confirmarPassword) {
      this.mensajeError = 'Las contraseñas no coinciden.';
      return;
    }
    
    this.procesando = true;
    this.authService.actualizarPassword(this.correoRecuperacion, this.nuevaPassword).subscribe({
      next: () => {
        this.mensajeExito = 'Contraseña actualizada. Ya puedes iniciar sesión.';
        this.vistaActiva = 0;
        this.correo = this.correoRecuperacion;
        this.password = '';
        this.procesando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.mensajeError = 'Ocurrió un error al actualizar.';
        this.procesando = false;
        this.cdr.detectChanges();
      }
    });
  }


  cambiarVista(vista: number) {
    this.vistaActiva = vista;
    this.mensajeError = '';
    this.mensajeExito = '';
    this.nuevaPassword = '';
    this.confirmarPassword = '';
    this.cdr.detectChanges();
  }

}