import { HttpInterceptorFn } from '@angular/common/http';

export const autenticacionInterceptor: HttpInterceptorFn = (req, next) => {
    if (req.url.includes('/auth/')) {
        return next(req);
    }

    const usuarioGuardado = localStorage.getItem('usuarioLogueado');
    let token = '';

    if (usuarioGuardado) {
        const datos = JSON.parse(usuarioGuardado);
        token = datos.token; 
    }

    if (token) {
        const peticionClonada = req.clone({
            setHeaders: {
                Authorization: 'Bearer ' + token
            }
        });
        return next(peticionClonada);
    }

    return next(req);
};