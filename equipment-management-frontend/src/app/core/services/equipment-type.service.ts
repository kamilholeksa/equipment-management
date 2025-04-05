import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EquipmentTypeModel } from '../models/equipment-type/equipment-type.model';

@Injectable({
  providedIn: 'root',
})
export class EquipmentTypeService {
  private apiUrl = 'http://localhost:8080/api/equipment-type';

  constructor(private http: HttpClient) {}

  getAllEquipmentTypes(): Observable<EquipmentTypeModel[]> {
    return this.http.get<EquipmentTypeModel[]>(this.apiUrl);
  }

  createEquipmentType(
    equipmentType: EquipmentTypeModel,
  ): Observable<EquipmentTypeModel> {
    return this.http.post<EquipmentTypeModel>(this.apiUrl, equipmentType);
  }

  updateEquipmentType(
    id: number,
    equipmentType: EquipmentTypeModel,
  ): Observable<EquipmentTypeModel> {
    return this.http.put<EquipmentTypeModel>(
      this.apiUrl + `/${id}`,
      equipmentType,
    );
  }
}
