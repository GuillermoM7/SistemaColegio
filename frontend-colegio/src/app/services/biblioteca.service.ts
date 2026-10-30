import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})

export class BibliotecaService {
    
    private apiUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/biblioteca';

    constructor(private http: HttpClient) {}

    obtenerLibros(): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/libros`);
    }


    registrarLibro(libro: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/libros`, libro);
    }


    actualizarLibro(id: number, libro: any): Observable<any> {
        return this.http.put(`${this.apiUrl}/libros/${id}`, libro);
    }


    eliminarLibro(id: number): Observable<any> {
        return this.http.delete(`${this.apiUrl}/libros/${id}`);
    }
    

    buscarUsuariosPrestamo(query: string): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/usuarios/buscar?q=${query}`);
    }


    obtenerPrestamosActivos(idLibro: number): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/libros/${idLibro}/prestamos`);
    }

    
    registrarPrestamo(idLibro: number, prestamoData: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/libros/${idLibro}/prestamos`, prestamoData);
    }


    obtenerPrestamosUsuario(idUsuario: number): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/usuarios/${idUsuario}/prestamos`);
    }

    
    procesarDevolucion(data: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/devoluciones`, data);
    }


    obtenerMultasUsuario(idUsuario: number): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/usuarios/${idUsuario}/multas`);
    }

}