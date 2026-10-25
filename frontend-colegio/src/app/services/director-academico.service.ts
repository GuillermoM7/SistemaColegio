import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class DirectorAcademicoService {

    private apiUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/director/academico';
    private catalogoUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/configuracion/academico';


    constructor(private http: HttpClient) { }


    obtenerNiveles(): Observable<any[]> {
        return this.http.get<any[]>(`${this.catalogoUrl}/niveles`);
    }


    obtenerCarrerasActivas(): Observable<any[]> {
        return this.http.get<any[]>(`${this.catalogoUrl}/carreras`);
    }


    obtenerGradosConCurriculo(idNivel: number, idCarrera?: number): Observable<any[]> {
        let params = new HttpParams();
        if (idCarrera) {
            params = params.set('idCarrera', idCarrera.toString());
        }
        return this.http.get<any[]>(`${this.apiUrl}/grados-curriculo/${idNivel}`, { params });
    }

    
    guardarCurriculo(datos: { idGrado: number, cursosIds: number[] }): Observable<any> {
        return this.http.post(`${this.apiUrl}/curriculos`, datos);
    }


    obtenerSeccionesPorGrado(idGrado: number): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/secciones/grado/${idGrado}`);
    }


    obtenerCursosCurriculoActivo(idGrado: number): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/curriculos/grados/${idGrado}/cursos`);
    } 


    obtenerMaestros(): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/maestros`);
    }

    
    guardarSeccion(datos: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/secciones`, datos);
    }
    
}