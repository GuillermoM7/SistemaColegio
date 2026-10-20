import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


export interface RegistroEmpleadoDTO {
    correo: string;
    password?: string; 
    idRol: number;
    nombres: string;
    apellidos: string;
    puesto: string;
    fechaContratacion: string; // Formato YYYY-MM-DD
    salario: number;
}

export interface ListadoEmpleadoDTO {
    idUsuario: number;
    correo: string;
    idRol: number;
    estado: boolean;
    idEmpleado: number;
    nombre: string;
    apellidos: string;
    puesto: string;
    fechaContratacion: string;
    salario: number;
}

export interface PaginaEmpleadosDTO {
    empleados: ListadoEmpleadoDTO[];
    totalRegistros: number;
    totalPaginas: number;
}

@Injectable({
    providedIn: 'root'
})

export class GestionPersonalService {
    private apiUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/gestion-personal';

    constructor(private http: HttpClient) { }


    registrarEmpleado(empleado: RegistroEmpleadoDTO): Observable<any> {
        return this.http.post(`${this.apiUrl}/registrar-empleado`, empleado);
    }


    listarEmpleados(pagina: number, limite: number = 5): Observable<PaginaEmpleadosDTO> {
        return this.http.get<PaginaEmpleadosDTO>(`${this.apiUrl}/listar?pagina=${pagina}&limite=${limite}`);
    }


    cambiarEstadoEmpleado(idUsuario: number, nuevoEstado: boolean): Observable<any> {
        return this.http.put(`${this.apiUrl}/cambiar-estado/${idUsuario}?estado=${nuevoEstado}`, {});
    }


    modificarEmpleado(empleado: ListadoEmpleadoDTO): Observable<any> {
        return this.http.put(`${this.apiUrl}/modificar`, empleado);
    }
}