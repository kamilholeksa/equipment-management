import { Component, Inject } from '@angular/core';
import { DatePipe, NgClass } from '@angular/common';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { ServiceRequest } from '../../models/service-request.model';
import { ServiceRequestStatusEnum } from '../../../../shared/enums/service-request-status.enum';
import { MatButtonModule } from '@angular/material/button';
import { ServiceRequestService } from '../../services/service-request.service';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import {ServiceRequestStatusDisplayPipe} from '../../../../shared/pipes/service-request-status-display.pipe';

@Component({
  selector: 'app-service-request-details-dialog',
  standalone: true,
  imports: [
    DatePipe,
    MatDialogModule,
    MatButtonModule,
    NgClass,
    ServiceRequestStatusDisplayPipe,
  ],
  templateUrl: './service-request-details-dialog.component.html',
  styleUrl: './service-request-details-dialog.component.scss',
})
export class ServiceRequestDetailsDialogComponent {
  serviceRequest: ServiceRequest;
  protected readonly ServiceRequestStatusEnum = ServiceRequestStatusEnum;

  constructor(
    private serviceRequestService: ServiceRequestService,
    private dialogRef: MatDialogRef<ServiceRequestDetailsDialogComponent>,
    private notificationService: NotificationService,
    @Inject(MAT_DIALOG_DATA)
    public data: {
      serviceRequest: ServiceRequest;
    },
  ) {
    this.serviceRequest = data.serviceRequest;
  }

  cancelServiceRequest() {
    const confirmed = confirm(
      'Are you sure you want to cancel this service request?',
    );

    if (confirmed) {
      this.serviceRequestService
        .cancelServiceRequest(this.serviceRequest.id)
        .subscribe({
          next: () => {
            this.notificationService.success(
              'Service request has been cancelled',
            );
            this.dialogRef.close(true);
          },
          error: () => this.notificationService.error(),
        });
    }
  }

  getStatusClass(status: string): string {
    return this.serviceRequestService.getStatusClass(status);
  }
}
