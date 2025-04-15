import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EquipmentHistory } from '../models/equipment-history.model';

@Injectable({
  providedIn: 'root',
})
export class EquipmentHistoryService {
  private apiUrl = 'http://localhost:8080/api/equipment-history';

  constructor(private http: HttpClient) {}

  getHistoryByEquipmentId(id: number): Observable<EquipmentHistory[]> {
    return this.http.get<EquipmentHistory[]>(this.apiUrl + `/equipment/${id}`);
  }
}
