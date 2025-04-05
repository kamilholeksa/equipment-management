import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Role } from '../models/role/role.model';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  private apiUrl = 'http://localhost:8080/api/role';

  constructor(private http: HttpClient) {}

  getAllRoles(): Observable<Role[]> {
    return this.http.get<Role[]>(this.apiUrl);
  }
}
