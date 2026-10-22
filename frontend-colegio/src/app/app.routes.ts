import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { PantallaPrincipal } from './components/pantalla-principal/pantalla-principal';
import { GestionPersonal } from './components/gestion-personal/gestion-personal';
import { ConfiguracionGlobalComponent } from './components/configuracion-global/configuracion-global';
import { rolGuard } from './services/rol.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  //pantallas
  { path: 'login', component: LoginComponent },
  { path: 'pantalla-principal', component: PantallaPrincipal,
    children: [
      {path: 'gestion-empleados-administrativos', component: GestionPersonal, canActivate: [rolGuard], data: { roles: [1] }},
      { path: 'configuracion', component: ConfiguracionGlobalComponent, canActivate: [rolGuard], data: { roles: [1] } }
  ]
  }
];