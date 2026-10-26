import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SecretariaInscripcionService {
    
    private apiUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/secretaria/inscripciones';

    constructor(private http: HttpClient) {}

    inscribirEstudiante(datos: any): Observable<any> {
        return this.http.post(this.apiUrl, datos);
    }
}