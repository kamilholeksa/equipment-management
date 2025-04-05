import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TransferModel } from '../models/transfer/transfer.model';
import { PageModel } from '../models/page/page.model';
import { AcceptTransferRequest } from '../models/transfer/accept-transfer-request.model';

@Injectable({
  providedIn: 'root',
})
export class TransferService {
  private apiUrl = 'http://localhost:8080/api/transfer';

  constructor(private http: HttpClient) {}

  createTransfer(transfer: TransferModel): Observable<TransferModel> {
    return this.http.post<TransferModel>(this.apiUrl, transfer);
  }

  getAllTransfers(
    page: number,
    size: number,
    sortField: string | null,
    sortOrder: string | null,
  ): Observable<PageModel<TransferModel>> {
    const params = new HttpParams()
      .set('pageNumber', page)
      .set('pageSize', size)
      .set('sortField', sortField ?? '')
      .set('sortOrder', sortOrder ?? '');

    return this.http.get<PageModel<TransferModel>>(this.apiUrl, {
      params,
    });
  }

  getMyTransfers(
    page: number,
    size: number,
    sortField: string | null,
    sortOrder: string | null,
  ): Observable<PageModel<TransferModel>> {
    const params = new HttpParams()
      .set('pageNumber', page)
      .set('pageSize', size)
      .set('sortField', sortField ?? '')
      .set('sortOrder', sortOrder ?? '');

    return this.http.get<PageModel<TransferModel>>(
      this.apiUrl + '/my-transfers',
      {
        params,
      },
    );
  }

  getTransfersToAccept(
    page: number,
    size: number,
    sortField: string | null,
    sortOrder: string | null,
  ): Observable<PageModel<TransferModel>> {
    const params = new HttpParams()
      .set('pageNumber', page)
      .set('pageSize', size)
      .set('sortField', sortField ?? '')
      .set('sortOrder', sortOrder ?? '');

    return this.http.get<PageModel<TransferModel>>(this.apiUrl + '/to-accept', {
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
}
