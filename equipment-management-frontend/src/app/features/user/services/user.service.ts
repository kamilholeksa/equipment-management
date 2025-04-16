import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserModel, UserSaveModel } from '../models/user.model';
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
    sortField: string | null,
    sortOrder: string | null,
  ): Observable<Page<UserModel>> {
    const params = new HttpParams()
      .set('pageNumber', page)
      .set('pageSize', size)
      .set('sortField', sortField ?? '')
      .set('sortOrder', sortOrder ?? '');

    return this.http.get<Page<UserModel>>(this.apiUrl, {
      params,
    });
  }

  getActiveUsers(): Observable<UserModel[]> {
    return this.http.get<UserModel[]>(this.apiUrl + '/active');
  }

  getActiveTechniciansUsers(): Observable<UserModel[]> {
    return this.http.get<UserModel[]>(this.apiUrl + '/active-technicians');
  }

  getCurrentUser(): Observable<UserModel> {
    return this.http.get<UserModel>(this.apiUrl + '/account');
  }

  getUser(id: number): Observable<UserModel> {
    return this.http.get<UserModel>(this.apiUrl + `/${id}`);
  }

  createUser(user: UserSaveModel): Observable<UserModel> {
    return this.http.post<UserModel>(this.apiUrl, user);
  }

  updateUser(id: number, user: UserSaveModel): Observable<UserModel> {
    return this.http.put<UserModel>(this.apiUrl + `/${id}`, user);
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
