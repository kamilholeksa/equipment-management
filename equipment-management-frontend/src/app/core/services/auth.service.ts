import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  userData = {
    firstName: '',
    lastName: '',
    email: '',
    username: '',
    roles: [] as string[],
  };

  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient) {
    this.userData.firstName = this.getJwtData().firstName;
    this.userData.lastName = this.getJwtData().lastName;
    this.userData.email = this.getJwtData().email;
    this.userData.username = this.getJwtData().sub;
    this.userData.roles = this.getJwtData().roles;
  }

  getAuthToken(): string | null {
    return window.localStorage.getItem('jwt_token');
  }

  setAuthToken(token: string | null): void {
    if (token !== null) {
      window.localStorage.setItem('jwt_token', token);
    } else {
      window.localStorage.removeItem('jwt_token');
    }
  }

  getJwtData(): any {
    const token = this.getAuthToken();
    return token != null ? this.jwtHelper.decodeToken(token) : [];
    // return token != null ? jwtDecode(token) : [];
  }

  login(credentials: any): Observable<any> {
    return this.http.post('http://localhost:8080/login', credentials);
  }

  logout() {
    window.localStorage.removeItem('jwt_token');
  }

  isLoggedIn(): boolean {
    return !!this.getAuthToken();
  }

  hasRole(role: string): boolean {
    return this.userData.roles.includes(role);
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some((role) => this.hasRole(role));
  }
}
