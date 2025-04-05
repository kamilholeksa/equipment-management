import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EquipmentModel } from '../models/equipment/equipment.model';
import { PageModel } from '../models/page/page.model';

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
  ): Observable<PageModel<EquipmentModel>> {
    const params = new HttpParams()
      .set('pageNumber', page)
      .set('pageSize', size)
      .set('sortField', sortField ?? '')
      .set('sortOrder', sortOrder ?? '');

    return this.http.get<PageModel<EquipmentModel>>(this.apiUrl, {
      params,
    });
  }

  getEquipment(id: number): Observable<EquipmentModel> {
    return this.http.get<EquipmentModel>(this.apiUrl + `/${id}`);
  }

  getCurrentUserEquipment(
    page: number,
    size: number,
    sortField?: string | null,
    sortOrder?: string | null,
  ): Observable<PageModel<EquipmentModel>> {
    const params = new HttpParams()
      .set('pageNumber', page)
      .set('pageSize', size)
      .set('sortField', sortField ?? '')
      .set('sortOrder', sortOrder ?? '');

    return this.http.get<PageModel<EquipmentModel>>(
      this.apiUrl + '/my-equipment',
      { params },
    );
  }

  createEquipment(equipment: EquipmentModel) {
    return this.http.post<EquipmentModel>(this.apiUrl, equipment);
  }

  updateEquipment(id: number, equipment: EquipmentModel) {
    return this.http.put<EquipmentModel>(this.apiUrl + `/${id}`, equipment);
  }

  decommission(id: number) {
    return this.http.patch(this.apiUrl + `/${id}/decommission`, {});
  }
}
