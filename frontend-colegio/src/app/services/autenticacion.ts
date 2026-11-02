import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/auth';

  constructor(private http: HttpClient) { }

  login(correo: string, password: string): Observable<any> {
    const credenciales = { 
      correo: correo, 
      password: password 
    };

    return this.http.post<any>(`${this.apiUrl}/login`, credenciales);
  }


  recuperarPassword(correo: string) {
    return this.http.post(`${this.apiUrl}/recuperar`, { correo });
  }


  verificarCorreo(correo: string) {
    return this.http.post(`${this.apiUrl}/verificar-correo`, { correo });
  }


  actualizarPassword(correo: string, nuevaPassword: string) {
    return this.http.put(`${this.apiUrl}/actualizar-password`, { correo, nuevaPassword });
  }

}