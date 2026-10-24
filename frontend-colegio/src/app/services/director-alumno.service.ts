import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class DirectorAlumnoService {

    private apiUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/director/alumnos';

    constructor(private http: HttpClient) { }

    
    listarSinUsuario(): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/sin-usuario`);
    }


    listarPaginados(pagina: number, limite: number = 5): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}?pagina=${pagina}&limite=${limite}`);
    }


    crearUsuario(datos: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/crear-usuario`, datos);
    }


    cambiarEstado(idUsuario: number, estado: boolean): Observable<any> {
        return this.http.put(`${this.apiUrl}/cambiar-estado/${idUsuario}?estado=${estado}`, {});
    }

}