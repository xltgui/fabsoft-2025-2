import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { UserService } from '../../service/user-service';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const userService = inject(UserService);
  const router = inject(Router);

  const token = userService.getToken();

  if(token){
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    })
  }

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      
      // Verifica se é 401 (Unauthorized) ou 403 (Forbidden) E a URL não é de login
      if ((error.status === 401 || error.status === 403) && !req.url.includes('/auth/login')) {
        
        // Limpa tudo e redireciona
        userService.cleanUpAuth(); 
        // Assumindo que sua rota de login é '/login'
        router.navigate(['users/login']); 

        console.error('Sessão expirada ou acesso negado. Token removido.');
      }
      
      return throwError(() => error); 
    })
  );
};
