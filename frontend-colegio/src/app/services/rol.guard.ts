import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { jwtDecode } from 'jwt-decode'; 

export const rolGuard: CanActivateFn = (route, state) => {
    const router = inject(Router);

    const usuarioGuardado = localStorage.getItem('usuarioLogueado');

    if (!usuarioGuardado) {
    router.navigate(['/login']);
    return false;
    }

    try {
    const datosGuardados = JSON.parse(usuarioGuardado);
    const token = datosGuardados.token;
    const tokenDecodificado: any = jwtDecode(token);
    const rolUsuario = tokenDecodificado.idRol; 

    const rolesPermitidos = route.data['roles'] as Array<number>;

    if (rolesPermitidos && !rolesPermitidos.includes(rolUsuario)) {
        alert('Acceso Denegado: Tu rol no tiene permisos para acceder a este módulo.');
        router.navigate(['/pantalla-principal']); 
        return false;
    }

    return true;

    } catch (error) {
    console.error('Error al decodificar el token en el Guard:', error);
    router.navigate(['/login']);
    return false;
    }
};