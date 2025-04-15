import { Component, Inject } from '@angular/core';
import { DatePipe, NgIf } from '@angular/common';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { ServiceRequestModel } from '../../../core/models/service-request/service-request.model';
import { TransferStatusDisplayPipe } from '../../../core/pipes/service-request-status-display.pipe';
import { ServiceRequestStatusEnum } from '../../../core/enums/service-request-status.enum';
import { MatButtonModule } from '@angular/material/button';
import { ServiceRequestService } from '../../../core/services/service-request.service';
import { NotificationService } from '../../../core/services/shared/notification.service';

@Component({
  selector: 'app-service-request-details-dialog',
  standalone: true,
  imports: [
    DatePipe,
    MatDialogModule,
    MatButtonModule,
    TransferStatusDisplayPipe,
    NgIf,
  ],
  templateUrl: './service-request-details-dialog.component.html',
  styleUrl: './service-request-details-dialog.component.scss',
})
export class ServiceRequestDetailsDialogComponent {
  serviceRequest: ServiceRequestModel;
  protected readonly ServiceRequestStatusEnum = ServiceRequestStatusEnum;

  constructor(
    private serviceRequestService: ServiceRequestService,
    private dialogRef: MatDialogRef<ServiceRequestDetailsDialogComponent>,
    private notificationService: NotificationService,
    @Inject(MAT_DIALOG_DATA)
    public data: {
      serviceRequest: ServiceRequestModel;
    },
  ) {
    this.serviceRequest = data.serviceRequest;
  }

  cancelServiceRequest() {
    const confirmed = confirm('Czy na pewno chcesz anulować zgłoszenie?');

    this.serviceRequestService
      .cancelServiceRequest(this.serviceRequest.id)
      .subscribe({
        next: () => {
          this.notificationService.showSuccess('Anulowano zgłoszenie');
          this.dialogRef.close();
          window.location.reload();
        },
        error: () => this.notificationService.showError('Wystąpił błąd'),
      });
  }
}
