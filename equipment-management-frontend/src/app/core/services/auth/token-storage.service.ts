import { Injectable } from '@angular/core';
import {AuthResponse} from '../../models/auth/auth-response.model';

const ACCESS_TOKEN_KEY = 'access_token';
const REFRESH_TOKEN_KEY = 'refresh_token';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  constructor() { }

  getAccessToken(): string | null {
    return window.localStorage.getItem(ACCESS_TOKEN_KEY);
  }

  setAccessToken(token: string | null): void {
    if (token !== null) {
      window.localStorage.setItem(ACCESS_TOKEN_KEY, token);
    } else {
      window.localStorage.removeItem(ACCESS_TOKEN_KEY);
    }
  }

  getRefreshToken(): string {
    return window.localStorage.getItem(REFRESH_TOKEN_KEY) ?? '';
  }

  setRefreshToken(token: string | null): void {
    if (token !== null) {
      window.localStorage.setItem(REFRESH_TOKEN_KEY, token);
    } else {
      window.localStorage.removeItem(REFRESH_TOKEN_KEY);
    }
  }

  storeTokens({ accessToken, refreshToken }: AuthResponse) {
    window.localStorage.setItem(ACCESS_TOKEN_KEY, accessToken);
    window.localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
  }

  clearTokens() {
    window.localStorage.removeItem(ACCESS_TOKEN_KEY);
    window.localStorage.removeItem(REFRESH_TOKEN_KEY);
  }
}
