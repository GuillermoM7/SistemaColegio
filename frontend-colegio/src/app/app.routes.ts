import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { PantallaPrincipal } from './components/pantalla-principal/pantalla-principal';
import { GestionPersonal } from './components/gestion-personal/gestion-personal';
import { ConfiguracionGlobalComponent } from './components/configuracion-global/configuracion-global';
import { DirectorPersonalComponent } from './components/director-personal.component/director-personal.component';
import { rolGuard } from './services/rol.guard';
import { DirectorAlumnosComponent } from './components/director-alumnos.component/director-alumnos.component';
import { DirectorCursosComponent } from './components/director-cursos.component/director-cursos.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  //pantallas
  { path: 'login', component: LoginComponent },
  { path: 'pantalla-principal', component: PantallaPrincipal,
    children: [
      {path: 'gestion-empleados-administrativos', component: GestionPersonal, canActivate: [rolGuard], data: { roles: [1] }},
      {path: 'configuracion', component: ConfiguracionGlobalComponent, canActivate: [rolGuard], data: { roles: [1] } },
      {path: 'director/personal', component: DirectorPersonalComponent, canActivate: [rolGuard], data: { roles: [2] } },
      {path: 'director/alumnos', component: DirectorAlumnosComponent, canActivate: [rolGuard], data: { roles: [2] } },
      {path: 'director/cursos', component: DirectorCursosComponent, canActivate: [rolGuard], data: { roles: [2] } }
  ]
  }
];