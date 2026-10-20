import { HttpInterceptorFn } from '@angular/common/http';

export const autenticacionInterceptor: HttpInterceptorFn = (req, next) => {
    const usuarioGuardado = localStorage.getItem('usuarioLogueado');
    let token = '';

    if (usuarioGuardado) {
        const datos = JSON.parse(usuarioGuardado);
        token = datos.token; 
    }

    if (token && !req.url.includes('/login')) {
        const peticionClonada = req.clone({
        setHeaders: {
            Authorization: 'Bearer ' + token
        }
        });
        return next(peticionClonada);
    }

    return next(req);
};