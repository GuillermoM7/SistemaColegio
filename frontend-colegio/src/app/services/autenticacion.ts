import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/auth/login';

  constructor(private http: HttpClient) { }

  login(correo: string, password: string): Observable<any> {
    const credenciales = { 
      correo: correo, 
      password: password 
    };

    return this.http.post<any>(this.apiUrl, credenciales);
  }
}