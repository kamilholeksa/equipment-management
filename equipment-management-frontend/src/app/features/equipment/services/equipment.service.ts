import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../../../shared/models/page.model';
import { Equipment } from '../models/equipment.model';
import { EquipmentFilter } from '../models/equipment-filter.model';

@Injectable({
  providedIn: 'root',
})
export class EquipmentService {
  private apiUrl = 'http://localhost:8080/api/equipment';

  constructor(private http: HttpClient) {}

  getAllEquipment(
    page: number,
    size: number,
    sort?: string | null,
    filters?: EquipmentFilter,
  ): Observable<Page<Equipment>> {
    const params = this.prepareHttpParams(page, size, sort, filters);
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
    sort?: string | null,
    filters?: EquipmentFilter,
  ): Observable<Page<Equipment>> {
    const params = this.prepareHttpParams(page, size, sort, filters);
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

  getStatusClass(status: string): string {
    switch (status) {
      case 'NEW':
        return 'status-badge eq-new';
      case 'IN_PREPARATION':
        return 'status-badge eq-in-preparation';
      case 'IN_USE':
        return 'status-badge eq-in-use';
      case 'IN_REPAIR':
        return 'status-badge eq-in-repair';
      case 'RESERVE':
        return 'status-badge eq-reserve';
      case 'DECOMMISSIONED':
        return 'status-badge eq-decommissioned';
      default:
        return '';
    }
  }

  private prepareHttpParams(
    page: number,
    size: number,
    sort?: string | null,
    filters?: EquipmentFilter,
  ): HttpParams {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort ?? '');

    if (filters) {
      Object.entries(filters).forEach(([key, value]) => {
        if (
          value !== null &&
          value !== '' &&
          !(Array.isArray(value) && value.length === 0)
        ) {
          params = params.set(key, value);
        }
      });
    }

    return params;
  }
}
