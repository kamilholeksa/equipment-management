import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../../../shared/models/page.model';
import {
  ServiceRequest,
  ServiceRequestWithNotesModel,
} from '../models/service-request.model';

@Injectable({
  providedIn: 'root',
})
export class ServiceRequestService {
  private apiUrl = 'http://localhost:8080/api/service-request';

  constructor(private http: HttpClient) {}

  getAllServiceRequests(
    page: number,
    size: number,
    sortField: string | null,
    sortOrder: string | null,
  ): Observable<Page<ServiceRequest>> {
    const params = new HttpParams()
      .set('pageNumber', page)
      .set('pageSize', size)
      .set('sortField', sortField ?? '')
      .set('sortOrder', sortOrder ?? '');

    return this.http.get<Page<ServiceRequest>>(this.apiUrl, {
      params,
    });
  }

  getOpenServiceRequests(
    page: number,
    size: number,
    sortField: string | null,
    sortOrder: string | null,
  ): Observable<Page<ServiceRequest>> {
    const params = new HttpParams()
      .set('pageNumber', page)
      .set('pageSize', size)
      .set('sortField', sortField ?? '')
      .set('sortOrder', sortOrder ?? '');

    return this.http.get<Page<ServiceRequest>>(
      this.apiUrl + '/open',
      {
        params,
      },
    );
  }

  getServiceRequestsForEquipment(
    equipmentId: number,
    page: number,
    size: number,
    sortField: string | null,
    sortOrder: string | null,
  ) {
    const params = new HttpParams()
      .set('pageNumber', page)
      .set('pageSize', size)
      .set('sortField', sortField ?? '')
      .set('sortOrder', sortOrder ?? '');

    return this.http.get<Page<ServiceRequest>>(
      this.apiUrl + `/equipment/${equipmentId}`,
      {
        params,
      },
    );
  }

  getServiceRequest(id: number): Observable<ServiceRequestWithNotesModel> {
    return this.http.get<ServiceRequestWithNotesModel>(this.apiUrl + `/${id}`);
  }

  createServiceRequest(
    serviceRequest: ServiceRequest,
  ): Observable<ServiceRequest> {
    return this.http.post<ServiceRequest>(this.apiUrl, serviceRequest);
  }

  accept(id: number): Observable<any> {
    return this.http.patch(this.apiUrl + `/${id}/accept`, {});
  }

  closeServiceRequest(id: number, closeInfo: string) {
    return this.http.patch(this.apiUrl + `/${id}/close`, closeInfo);
  }

  assignTechnician(serviceRequestId: number, userId: number) {
    return this.http.patch(this.apiUrl + `/${serviceRequestId}/assign`, userId);
  }

  cancelServiceRequest(serviceRequestId: number) {
    return this.http.patch(this.apiUrl + `/${serviceRequestId}/cancel`, {});
  }
}
