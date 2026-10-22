import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Nivel {
    idNivel: number;
    nombre: string;
}

export interface Grado {
    idGrado?: number;
    nombre: string;
    idNivel: number;
    idCarrera?: number;
    nombreCarrera?: string;
}

export interface Carrera {
    idCarrera?: number;
    nombre: string;
    activa: boolean;
}

@Injectable({
    providedIn: 'root'
})
export class AcademicoService {

    private apiUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/configuracion/academico';

    constructor(private http: HttpClient) { }


    obtenerNiveles(): Observable<Nivel[]> {
        return this.http.get<Nivel[]>(`${this.apiUrl}/niveles`);
    }


    obtenerGradosPorNivel(idNivel: number): Observable<Grado[]> {
        return this.http.get<Grado[]>(`${this.apiUrl}/grados/nivel/${idNivel}`);
    }


    obtenerCarreras(): Observable<Carrera[]> {
        return this.http.get<Carrera[]>(`${this.apiUrl}/carreras`);
    }


    cambiarEstadoCarrera(idCarrera: number, estado: boolean): Observable<any> {
        return this.http.put(`${this.apiUrl}/carreras/estado/${idCarrera}/${estado}`, {});
    }


    crearCarrera(carrera: Carrera): Observable<any> {
        return this.http.post(`${this.apiUrl}/crear-carreras`, carrera);
    }


    crearGrado(grado: Grado): Observable<any> {
        return this.http.post(`${this.apiUrl}/grados`, grado);
    }
}