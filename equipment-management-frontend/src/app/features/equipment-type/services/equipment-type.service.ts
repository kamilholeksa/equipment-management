import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EquipmentType } from '../models/equipment-type.model';

@Injectable({
  providedIn: 'root',
})
export class EquipmentTypeService {
  private apiUrl = 'http://localhost:8080/api/equipment-type';

  constructor(private http: HttpClient) {}

  getAllEquipmentTypes(): Observable<EquipmentType[]> {
    return this.http.get<EquipmentType[]>(this.apiUrl);
  }

  createEquipmentType(equipmentType: EquipmentType): Observable<EquipmentType> {
    return this.http.post<EquipmentType>(this.apiUrl, equipmentType);
  }

  updateEquipmentType(
    id: number,
    equipmentType: EquipmentType,
  ): Observable<EquipmentType> {
    return this.http.put<EquipmentType>(this.apiUrl + `/${id}`, equipmentType);
  }

  deleteEquipmentType(typeId: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl + `/${typeId}`);
  }
}
