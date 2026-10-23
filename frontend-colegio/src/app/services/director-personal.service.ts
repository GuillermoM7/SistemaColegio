import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaginaEmpleadosDTO, RegistroEmpleadoDTO, ListadoEmpleadoDTO } from './gestion-personal';

@Injectable({
  providedIn: 'root'
})
export class DirectorPersonalService {

  private apiUrl = 'http://localhost:8080/Proyecto2Backend/api/v1/director/personal';

  constructor(private http: HttpClient) { }

  listarPersonalOperativo(pagina: number, limite: number = 5): Observable<PaginaEmpleadosDTO> {
    return this.http.get<PaginaEmpleadosDTO>(`${this.apiUrl}?pagina=${pagina}&limite=${limite}`);
  }

  crearPersonalOperativo(empleado: RegistroEmpleadoDTO): Observable<any> {
    return this.http.post(`${this.apiUrl}`, empleado);
  }

  modificarPersonalOperativo(empleado: ListadoEmpleadoDTO): Observable<any> {
    return this.http.put(`${this.apiUrl}`, empleado);
  }
}