import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../../../shared/models/page.model';
import {
  ServiceRequest,
  ServiceRequestWithNotesModel,
} from '../models/service-request.model';
import { ServiceRequestFilter } from '../models/service-request-filter.model';

@Injectable({
  providedIn: 'root',
})
export class ServiceRequestService {
  private apiUrl = 'http://localhost:8080/api/service-request';

  constructor(private http: HttpClient) {}

  getAllServiceRequests(
    page: number,
    size: number,
    sort?: string | null,
    filters?: ServiceRequestFilter,
  ): Observable<Page<ServiceRequest>> {
    const params = this.prepareHttpParams(page, size, sort, filters);
    return this.http.get<Page<ServiceRequest>>(this.apiUrl, {
      params,
    });
  }

  getOpenServiceRequests(
    page: number,
    size: number,
    sort?: string | null,
    filters?: ServiceRequestFilter,
  ): Observable<Page<ServiceRequest>> {
    const params = this.prepareHttpParams(page, size, sort, filters);
    return this.http.get<Page<ServiceRequest>>(this.apiUrl + '/open', {
      params,
    });
  }

  getServiceRequestsForEquipment(
    equipmentId: number,
    page: number,
    size: number,
    sort?: string | null,
  ) {
    const params = this.prepareHttpParams(page, size, sort);
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

  getStatusClass(status: string): string {
    switch (status) {
      case 'NEW':
        return 'status-badge sr-new';
      case 'ACCEPTED':
        return 'status-badge sr-accepted';
      case 'IN_PROGRESS':
        return 'status-badge sr-in-progress';
      case 'CANCELLED':
        return 'status-badge sr-cancelled';
      case 'CLOSED':
        return 'status-badge sr-closed';
      default:
        return '';
    }
  }

  private prepareHttpParams(
    page: number,
    size: number,
    sort?: string | null,
    filters?: ServiceRequestFilter,
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
