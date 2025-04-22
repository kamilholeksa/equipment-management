import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { ServiceRequestService } from '../../services/service-request.service';
import {
  FormControl,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import { DialogRef } from '@angular/cdk/dialog';
import { MatButton } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-service-request-close-dialog',
  standalone: true,
  imports: [
    MatButton,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './service-request-close-dialog.component.html',
  styleUrl: './service-request-close-dialog.component.scss',
})
export class ServiceRequestCloseDialogComponent {
  closeInfo: FormControl;

  constructor(
    private dialogRef: DialogRef<ServiceRequestCloseDialogComponent>,
    private serviceRequestService: ServiceRequestService,
    private notificationService: NotificationService,
    @Inject(MAT_DIALOG_DATA)
    public data: { serviceRequestId: number },
  ) {
    this.closeInfo = new FormControl('', Validators.required);
  }

  onSubmit() {
    if (this.closeInfo.valid) {
      const data = this.closeInfo.value;

      this.serviceRequestService
        .closeServiceRequest(this.data.serviceRequestId, data)
        .subscribe({
          next: () => {
            this.notificationService.success('Service request has been closed');
            this.dialogRef.close();
            window.location.reload();
          },
          error: () => this.notificationService.error(),
        });
    }
  }
}
