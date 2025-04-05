import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EquipmentHistoryModel } from '../models/equipment-history/equipment-history.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EquipmentHistoryService {
  private apiUrl = 'http://localhost:8080/api/equipment-history';

  constructor(private http: HttpClient) {}

  getHistoryByEquipmentId(id: number): Observable<EquipmentHistoryModel[]> {
    return this.http.get<EquipmentHistoryModel[]>(
      this.apiUrl + `/equipment/${id}`,
    );
  }
}
