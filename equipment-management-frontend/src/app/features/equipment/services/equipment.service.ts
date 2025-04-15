import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../../../shared/models/page.model';
import { Equipment } from '../models/equipment.model';

@Injectable({
  providedIn: 'root',
})
export class EquipmentService {
  private apiUrl = 'http://localhost:8080/api/equipment';

  constructor(private http: HttpClient) {}

  getAllEquipment(
    page: number,
    size: number,
    sortField: string | null,
    sortOrder: string | null,
  ): Observable<Page<Equipment>> {
    const params = new HttpParams()
      .set('pageNumber', page)
      .set('pageSize', size)
      .set('sortField', sortField ?? '')
      .set('sortOrder', sortOrder ?? '');

    return this.http.get<Page<Equipment>>(this.apiUrl, {
      params,
    });
  }

  getEquipment(id: number): Observable<Equipment> {
    return this.http.get<Equipment>(this.apiUrl + `/${id}`);
  }

  getCurrentUserEquipment(
    page: number,
    size: number,
    sortField?: string | null,
    sortOrder?: string | null,
  ): Observable<Page<Equipment>> {
    const params = new HttpParams()
      .set('pageNumber', page)
      .set('pageSize', size)
      .set('sortField', sortField ?? '')
      .set('sortOrder', sortOrder ?? '');

    return this.http.get<Page<Equipment>>(this.apiUrl + '/my-equipment', {
      params,
    });
  }

  createEquipment(equipment: Equipment) {
    return this.http.post<Equipment>(this.apiUrl, equipment);
  }

  updateEquipment(id: number, equipment: Equipment) {
    return this.http.put<Equipment>(this.apiUrl + `/${id}`, equipment);
  }

  decommission(id: number) {
    return this.http.patch(this.apiUrl + `/${id}/decommission`, {});
  }
}
