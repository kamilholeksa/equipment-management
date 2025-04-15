import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../services/auth/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  const accessToken = authService.getAccessToken();
  if (accessToken) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${accessToken}` },
    });
  }

  return next(req).pipe(
    catchError((error) => {
      if (error.status === 401 && !req.url.includes('/refresh-token')) {
        return authService.refreshToken().pipe(
          switchMap(() => {
            const newToken = authService.getAccessToken();
            if (newToken) {
              const cloned = req.clone({
                setHeaders: { Authorization: `Bearer ${newToken}` },
              });
              return next(cloned);
            }
            return throwError(() => error);
          }),
          catchError(() => {
            authService.logout();
            return throwError(() => error);
          }),
        );
      }

      return throwError(() => error);
    }),
  );
};
