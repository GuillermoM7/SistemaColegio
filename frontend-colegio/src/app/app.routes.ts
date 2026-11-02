import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { PantallaPrincipal } from './components/pantalla-principal/pantalla-principal';
import { GestionPersonal } from './components/gestion-personal/gestion-personal';
import { ConfiguracionGlobalComponent } from './components/configuracion-global/configuracion-global';
import { DirectorPersonalComponent } from './components/director-personal.component/director-personal.component';
import { rolGuard } from './services/rol.guard';
import { InicioComponent } from './components/inicio.component/inicio.component';
import { DirectorAlumnosComponent } from './components/director-alumnos.component/director-alumnos.component';
import { DirectorCursosComponent } from './components/director-cursos.component/director-cursos.component';
import { GradosSeccionesComponent } from './components/grados-secciones.component/grados-secciones.component';
import { GestionEstudiantesComponent } from './components/gestion-estudiantes.component/gestion-estudiantes.component';
import { RealizarInscripcionComponent } from './components/realizar-inscripcion.component/realizar-inscripcion.component';
import { GestionMaestroComponent } from './components/gestion-maestro.component/gestion-maestro.component';
import { GestionBibliotecaComponent } from './components/gestion-biblioteca.component/gestion-biblioteca.component';
import { PrestamosBibliotecaComponent } from './components/prestamos-biblioteca.component/prestamos-biblioteca.component';
import { DevolucionesComponent } from './components/devoluciones.component/devoluciones.component';
import { CatalogoBibliotecaComponent } from './components/catalogo-biblioteca.component/catalogo-biblioteca.component';
import { MisNotasComponent } from './components/mis-notas.component/mis-notas.component';
import { MiPerfilComponent } from './components/mi-perfil.component/mi-perfil.component';
import { EstadoCuentaComponent } from './components/estado-cuenta.component/estado-cuenta.component';
import { ReporteSolvenciaComponent } from './components/reporte-solvencia.component/reporte-solvencia.component';
import { HistorialPagosComponent } from './components/historial-pagos.component/historial-pagos.component';
import { ReporteIngresosComponent } from './components/reporte-ingresos.component/reporte-ingresos.component';
import { ReporteMoraComponent } from './components/reporte-mora.component/reporte-mora.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  //pantallas
  { path: 'login', component: LoginComponent },
  { path: 'pantalla-principal', component: PantallaPrincipal,
    children: [
      { path: '', component: InicioComponent },
      {path: 'gestion-empleados-administrativos', component: GestionPersonal, canActivate: [rolGuard], data: { roles: [1] }},
      {path: 'configuracion', component: ConfiguracionGlobalComponent, canActivate: [rolGuard], data: { roles: [1] } },
      {path: 'director/personal', component: DirectorPersonalComponent, canActivate: [rolGuard], data: { roles: [2] } },
      {path: 'director/alumnos', component: DirectorAlumnosComponent, canActivate: [rolGuard], data: { roles: [2] } },
      {path: 'director/cursos', component: DirectorCursosComponent, canActivate: [rolGuard], data: { roles: [2] } },
      {path: 'director/grados/secciones', component: GradosSeccionesComponent, canActivate: [rolGuard], data: { roles: [2] } },
      {path: 'secretaria/gestion-estudiantes', component: GestionEstudiantesComponent, canActivate: [rolGuard], data: { roles: [3] } },
      {path: 'secretaria/inscripcion', component: RealizarInscripcionComponent, canActivate: [rolGuard], data: { roles: [3] } },
      {path: 'cursos/maestro', component: GestionMaestroComponent, canActivate: [rolGuard], data: { roles: [4] } },
      {path: 'biblioteca/gestion', component: GestionBibliotecaComponent, canActivate: [rolGuard], data: { roles: [6] } },
      {path: 'biblioteca/prestamos', component: PrestamosBibliotecaComponent, canActivate: [rolGuard], data: { roles: [6] } },
      {path: 'biblioteca/devoluciones', component: DevolucionesComponent, canActivate: [rolGuard], data: { roles: [6] } },
      {path: 'catalogo/biblioteca', component: CatalogoBibliotecaComponent, canActivate: [rolGuard], data: { roles: [4,5] } },
      {path: 'notas', component: MisNotasComponent, canActivate: [rolGuard], data: { roles: [5] } },
      {path: 'perfil', component: MiPerfilComponent, canActivate: [rolGuard], data: { roles: [5] } },
      {path: 'estado/cuentas', component: EstadoCuentaComponent, canActivate: [rolGuard], data: { roles: [5] } },
      {path: 'reporte/solvencia', component: ReporteSolvenciaComponent, canActivate: [rolGuard], data: { roles: [1,2,3] } },
      {path: 'reporte/historialPagos', component: HistorialPagosComponent, canActivate: [rolGuard], data: { roles: [1,2] } },
      {path: 'reporte/ingresos', component: ReporteIngresosComponent, canActivate: [rolGuard], data: { roles: [1] } },
      {path: 'reporte/mora', component: ReporteMoraComponent, canActivate: [rolGuard], data: { roles: [1,2,6] } }
  ]
  }
];