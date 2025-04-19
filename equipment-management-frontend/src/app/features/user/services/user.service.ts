import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User, UserSave } from '../models/user.model';
import {
  ChangeCurrentUserPasswordModel,
  ChangePasswordModel,
} from '../models/change-password.model';
import { Page } from '../../../shared/models/page.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient) {}

  getAllUsers(
    page: number,
    size: number,
    sort?: string | null,
  ): Observable<Page<User>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort ?? '');

    return this.http.get<Page<User>>(this.apiUrl, {
      params,
    });
  }

  getActiveUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl + '/active');
  }

  getActiveTechniciansUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl + '/active-technicians');
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(this.apiUrl + '/account');
  }

  getUser(id: number): Observable<User> {
    return this.http.get<User>(this.apiUrl + `/${id}`);
  }

  createUser(user: UserSave): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }

  updateUser(id: number, user: UserSave): Observable<User> {
    return this.http.put<User>(this.apiUrl + `/${id}`, user);
  }

  changePassword(id: number, data: ChangePasswordModel): Observable<any> {
    return this.http.patch<any>(this.apiUrl + `/${id}/change-password`, data);
  }

  changeCurrentUserPassword(
    data: ChangeCurrentUserPasswordModel,
  ): Observable<any> {
    return this.http.patch<any>(this.apiUrl + '/change-password', data);
  }

  toggleActive(id: number): Observable<any> {
    return this.http.patch(this.apiUrl + `/${id}/toggle-active`, {});
  }

  getStatusClass(active: boolean) {
    return active ? 'status-badge active' : 'status-badge inactive';
  }
}
