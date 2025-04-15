import { computed, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of, switchMap, tap } from 'rxjs';
import { AccountModel } from '../../models/auth/account.model';
import { TokenStorageService } from './token-storage.service';
import { AuthResponse } from '../../models/auth/auth-response.model';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly _account = signal<AccountModel | null>(null);
  readonly account = computed(() => this._account());

  constructor(
    private http: HttpClient,
    private tokenStorageService: TokenStorageService,
    private router: Router,
  ) {}

  login(username: string, password: string): Observable<AccountModel | null> {
    return this.http
      .post<AuthResponse>('http://localhost:8080/login', { username, password })
      .pipe(
        tap((res: AuthResponse) => this.tokenStorageService.storeTokens(res)),
        switchMap(() => this.fetchUserData()),
      );
  }

  refreshToken(): Observable<AuthResponse | null> {
    const refreshToken = this.tokenStorageService.getRefreshToken();
    if (!refreshToken) return of(null);

    return this.http
      .post<AuthResponse>('http://localhost:8080/refresh-token', {
        refreshToken,
      })
      .pipe(
        tap((res: AuthResponse) => this.tokenStorageService.storeTokens(res)),
        catchError(() => {
          this.logout();
          return of(null);
        }),
      );
  }

  fetchUserData(): Observable<AccountModel | null> {
    return this.http.get<AccountModel>('http://localhost:8080/account').pipe(
      tap((account: AccountModel) => this._account.set(account)),
      catchError(() => {
        this.logout();
        return of(null);
      }),
    );
  }

  getAccessToken(): string | null {
    return this.tokenStorageService.getAccessToken();
  }

  logout() {
    this._account.set(null);
    this.tokenStorageService.clearTokens();
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!this.tokenStorageService.getAccessToken();
  }

  hasRole(role: string): boolean {
    return !!this.account()?.roles.includes(role);
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some((role) => this.account()?.roles.includes(role));
  }
}
