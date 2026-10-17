import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; 
import { AuthService } from '../../services/autenticacion'; 
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule], 
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {

  correo = '';
  password = '';

  constructor(private authService: AuthService, private router: Router) {}


  ejecutarLogin() {

    this.authService.login(this.correo, this.password).subscribe({
      next: (respuestaBackend) => {
        localStorage.setItem('usuarioLogueado', JSON.stringify(respuestaBackend))
        this.router.navigate(['/pantalla-principal']);
      },
      error: (errorBackend) => {
        console.error('Error de credenciales', errorBackend);
        alert('Usuario o contraseña incorrectos.');
      }
    });
  }
}