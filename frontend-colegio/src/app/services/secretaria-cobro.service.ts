import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SecretariaCobroService {
    private apiUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/secretaria/cobros';

    constructor(private http: HttpClient) {}

    obtenerPendientes(idEstudiante: number): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/estudiantes/${idEstudiante}/pendientes`);
    }

    procesarPago(datos: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/pagar`, datos);
    }

    obtenerHistorial(idEstudiante: number): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/estudiantes/${idEstudiante}/historial`);
    }

    descargarReciboPdf(idPago: number): Observable<Blob> {
        return this.http.get(`${this.apiUrl}/${idPago}/recibo`, { responseType: 'blob' });
    }

    descargarSolvenciaPdf(idEstudiante: number): Observable<Blob> {
        return this.http.get(`${this.apiUrl}/estudiantes/${idEstudiante}/solvencia`, { responseType: 'blob' });
    }


    obtenerMultasPendientes(idEstudiante: number): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/multas-pendientes/${idEstudiante}`);
    }

    
    procesarPagoExtra(datos: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/pagar-extra`, datos);
    }


    procesarPagoMulta(datos: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/pagar-multa`, datos);
    }
}