import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class DirectorCursoService {

    private apiUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/director/cursos';

    constructor(private http: HttpClient) { }

    listarPaginados(pagina: number, limite: number = 5, busqueda: string = ''): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}?pagina=${pagina}&limite=${limite}&busqueda=${busqueda}`);
    }

    crearCurso(datos: { nombre: string }): Observable<any> {
        return this.http.post(this.apiUrl, datos);
    }
}