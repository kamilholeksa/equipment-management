import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {JwtHelperService} from '@auth0/angular-jwt';
import {AccountModel} from '../../models/auth/account.model';
import {TokenStorageService} from './token-storage.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  account!: AccountModel;

  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient, private tokenStorageService: TokenStorageService) {
    // const token = this.getAuthToken();
    // if (token && !this.jwtHelper.isTokenExpired(token)) {
    //   this.getAccount().subscribe({
    //     next: (account) => this.setAccount(account),
    //     error: () => this.logout()
    //   });
    // } else {
    //   this.logout();
    // }
  }

  getJwtData(): any {
    const token = this.tokenStorageService.getAccessToken();
    return token != null ? this.jwtHelper.decodeToken(token) : [];
    // return token != null ? jwtDecode(token) : [];
  }

  login(credentials: any): Observable<any> {
    return this.http.post('http://localhost:8080/login', credentials);
  }

  refreshToken(token: string) {
    return this.http.post('http://localhost:8080/refresh-token', token);
  }

  loadAccountData() {
    this.getAccount().subscribe({
      next: (account: AccountModel) => this.account = account,
    });
  }

  getAccount(): Observable<AccountModel> {
    return this.http.get<AccountModel>('http://localhost:8080/account');
  }

  setAccount(account: AccountModel) {
    this.account = account;
  }

  logout() {
    this.tokenStorageService.clearTokens();
  }

  isLoggedIn(): boolean {
    return !!this.tokenStorageService.getAccessToken();
  }

  hasRole(role: string): boolean {
    return this.account?.roles.includes(role) ?? false;
  }

  hasAnyRole(roles: string[]): boolean {
    return this.account?.roles.some((role) => roles.includes(role)) ?? false;
  }
}
