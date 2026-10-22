import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AnioLectivo {
    idAnioLectivo?: number; 
    anio: number;
    fechaInicio: string;
    fechaFin: string;
    activo: boolean;
}

@Injectable({
    providedIn: 'root'
})

export class ConfiguracionService {

    private apiUrlAnios = 'http://localhost:8080/Proyecto2Backend/api/v1/configuracion/anios';

    constructor(private http: HttpClient) { }


    
    obtenerAnios(): Observable<AnioLectivo[]> {
        return this.http.get<AnioLectivo[]>(this.apiUrlAnios);
    }


    crearAnio(anio: AnioLectivo): Observable<any> {
        return this.http.post(this.apiUrlAnios, anio);
    }


    activarAnio(idAnio: number): Observable<any> {
        return this.http.put(`${this.apiUrlAnios}/activar/${idAnio}`, {});
    }
    
}