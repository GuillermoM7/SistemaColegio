import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class EstudianteOperativoService {
    private apiUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/estudiante/operativo';

    constructor(private http: HttpClient) {}

    obtenerPerfil(idUsuario: number): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/perfil/${idUsuario}`);
    }


    obtenerEstadoCuenta(idUsuario: number): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/estado-cuenta/${idUsuario}`);
    }
    
}