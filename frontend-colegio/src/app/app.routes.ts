import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { PantallaPrincipal } from './components/pantalla-principal/pantalla-principal';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  //pantallas
  { path: 'login', component: LoginComponent },
  { path: 'pantalla-principal', component: PantallaPrincipal }
];