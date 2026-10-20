import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { PantallaPrincipal } from './components/pantalla-principal/pantalla-principal';
import { GestionPersonal } from './components/gestion-personal/gestion-personal';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  //pantallas
  { path: 'login', component: LoginComponent },
  { path: 'pantalla-principal', component: PantallaPrincipal,
    children: [
      {path: 'gestion-empleados', component: GestionPersonal}
  ]
  }
];