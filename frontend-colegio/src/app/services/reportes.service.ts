import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})

export class ReportesService {

    private apiUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/reportes';

    constructor(private http: HttpClient) { }

    obtenerReporteSolvencia(idGrado: number, idSeccion: number) {
    return this.http.get(`${this.apiUrl}/solvencia?idGrado=${idGrado}&idSeccion=${idSeccion}`, {
        responseType: 'blob'});
    }


    obtenerHistorialPagos(idEstudiante: number, fechaInicio: string, fechaFin: string) {
        return this.http.get(`${this.apiUrl}/historial-pagos?idEstudiante=${idEstudiante}&fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`, {
        responseType: 'blob'});
    }


    obtenerReporteIngresos(fechaInicio: string, fechaFin: string) {
        return this.http.get(`${this.apiUrl}/ingresos?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`, {
        responseType: 'blob'});
    }


    obtenerReporteMoraBiblioteca() {
        return this.http.get(`${this.apiUrl}/mora-biblioteca`, {
        responseType: 'blob'});
    }
    
}