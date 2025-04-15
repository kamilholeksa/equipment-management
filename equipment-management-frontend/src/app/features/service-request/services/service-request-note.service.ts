import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../../../shared/models/page.model';
import { ServiceRequestNote } from '../models/service-request-note.model';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ServiceRequestNoteService {
  private apiUrl = 'http://localhost:8080/api/service-request-note';

  constructor(private http: HttpClient) {}

  getServiceRequestNotes(
    id: number,
    page: number,
    size: number,
  ): Observable<Page<ServiceRequestNote>> {
    const params = new HttpParams()
      .set('pageNumber', page)
      .set('pageSize', size);

    return this.http.get<Page<ServiceRequestNote>>(
      this.apiUrl + `/service-request/${id}`,
      { params },
    );
  }

  createServiceRequestNote(
    data: ServiceRequestNote,
  ): Observable<ServiceRequestNote> {
    return this.http.post<ServiceRequestNote>(this.apiUrl, data);
  }

  updateServiceRequestNote(
    id: number,
    data: ServiceRequestNote,
  ): Observable<ServiceRequestNote> {
    return this.http.put<ServiceRequestNote>(this.apiUrl + `/${id}`, data);
  }

  deleteNote(id: number) {
    return this.http.delete(this.apiUrl + `/${id}`);
  }
}
