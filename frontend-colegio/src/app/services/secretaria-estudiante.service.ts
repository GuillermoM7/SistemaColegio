import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SecretariaEstudianteService {

    private apiUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/secretaria/estudiantes';

    constructor(private http: HttpClient) {}


    buscar(filtro: string): Observable<any[]> {
        const params = new HttpParams().set('q', filtro);
        return this.http.get<any[]>(`${this.apiUrl}/buscar`, { params });
    }


    crear(estudiante: any): Observable<any> {
        return this.http.post(this.apiUrl, estudiante);
    }


    actualizar(id: number, estudiante: any): Observable<any> {
        return this.http.put(`${this.apiUrl}/${id}`, estudiante);
    }

    
    cambiarEstado(id: number, estado: string): Observable<any> {
        return this.http.put(`${this.apiUrl}/${id}/estado`, { estado });
    }

    obtenerExpedienteAcademico(idEstudiante: number): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/${idEstudiante}/expediente`);
    }


    obtenerEstudiantePorUsuario(idUsuario: number): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/usuario/${idUsuario}`);
    }
    
}