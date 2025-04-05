import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AddressModel } from '../models/address/address.model';

@Injectable({
  providedIn: 'root',
})
export class AddressService {
  private apiUrl = 'http://localhost:8080/api/address';

  constructor(private http: HttpClient) {}

  getAllAddresses(): Observable<AddressModel[]> {
    return this.http.get<AddressModel[]>(this.apiUrl);
  }

  createAddress(address: AddressModel): Observable<AddressModel> {
    return this.http.post<AddressModel>(this.apiUrl, address);
  }

  updateAddress(id: number, address: AddressModel): Observable<AddressModel> {
    return this.http.put<AddressModel>(this.apiUrl + `/${id}`, address);
  }
}
