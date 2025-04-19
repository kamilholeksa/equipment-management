import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transfer } from '../models/transfer.model';
import { Page } from '../../../shared/models/page.model';
import { AcceptTransferRequest } from '../models/accept-transfer-request.model';

@Injectable({
  providedIn: 'root',
})
export class TransferService {
  private apiUrl = 'http://localhost:8080/api/transfer';

  constructor(private http: HttpClient) {}

  createTransfer(transfer: Transfer): Observable<Transfer> {
    return this.http.post<Transfer>(this.apiUrl, transfer);
  }

  getAllTransfers(
    page: number,
    size: number,
    sort?: string | null,
  ): Observable<Page<Transfer>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort ?? '');

    return this.http.get<Page<Transfer>>(this.apiUrl, {
      params,
    });
  }

  getMyTransfers(
    page: number,
    size: number,
    sort?: string | null,
  ): Observable<Page<Transfer>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort ?? '');

    return this.http.get<Page<Transfer>>(
      this.apiUrl + '/my-transfers',
      {
        params,
      },
    );
  }

  getTransfersToAccept(
    page: number,
    size: number,
    sort?: string | null,
  ): Observable<Page<Transfer>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort ?? '');

    return this.http.get<Page<Transfer>>(this.apiUrl + '/to-accept', {
      params,
    });
  }

  acceptTransfer(id: number, data: AcceptTransferRequest): Observable<any> {
    return this.http.post<AcceptTransferRequest>(
      this.apiUrl + `/${id}/accept`,
      data,
    );
  }

  rejectTransfer(id: number): Observable<any> {
    return this.http.post(this.apiUrl + `/${id}/reject`, {});
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'ACCEPTED':
        return 'status-badge tr-accepted';
      case 'PENDING':
        return 'status-badge tr-pending';
      case 'REJECTED':
        return 'status-badge tr-rejected';
      default:
        return '';
    }
  }
}
