import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth/auth.service';
import {TokenStorageService} from '../services/auth/token-storage.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private tokenStorageService: TokenStorageService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    const token = this.tokenStorageService.getAccessToken();
    let headers = {};

    if (token !== null) {
      headers = { Authorization: 'Bearer ' + token };
    }

    request = request.clone({ setHeaders: headers });
    return next.handle(request);
  }
}
