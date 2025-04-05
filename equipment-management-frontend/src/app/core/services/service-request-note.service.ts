import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageModel } from '../models/page/page.model';
import { ServiceRequestNoteModel } from '../models/service-request-note/service-request-note.model';
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
  ): Observable<PageModel<ServiceRequestNoteModel>> {
    const params = new HttpParams()
      .set('pageNumber', page)
      .set('pageSize', size);

    return this.http.get<PageModel<ServiceRequestNoteModel>>(
      this.apiUrl + `/service-request/${id}`,
      { params },
    );
  }

  createServiceRequestNote(
    data: ServiceRequestNoteModel,
  ): Observable<ServiceRequestNoteModel> {
    return this.http.post<ServiceRequestNoteModel>(this.apiUrl, data);
  }

  updateServiceRequestNote(
    id: number,
    data: ServiceRequestNoteModel,
  ): Observable<ServiceRequestNoteModel> {
    return this.http.put<ServiceRequestNoteModel>(this.apiUrl + `/${id}`, data);
  }

  deleteNote(id: number) {
    return this.http.delete(this.apiUrl + `/${id}`);
  }
}
