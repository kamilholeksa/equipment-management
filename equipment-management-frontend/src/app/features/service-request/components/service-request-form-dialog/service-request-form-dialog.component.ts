import { Component, Inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import { MatInputModule } from '@angular/material/input';
import { NgIf } from '@angular/common';
import { EquipmentService } from '../../../equipment/services/equipment.service';
import { ServiceRequestService } from '../../services/service-request.service';
import { Equipment } from '../../../equipment/models/equipment.model';

@Component({
  selector: 'app-service-request-form',
  standalone: true,
  imports: [
    MatDialogModule,
    FormsModule,
    MatButtonModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    NgIf,
  ],
  templateUrl: './service-request-form-dialog.component.html',
  styleUrl: './service-request-form-dialog.component.scss',
})
export class ServiceRequestFormDialogComponent {
  serviceRequestForm: FormGroup;
  equipment!: Equipment;

  constructor(
    private dialogRef: MatDialogRef<ServiceRequestFormDialogComponent>,
    private serviceRequestService: ServiceRequestService,
    private equipmentService: EquipmentService,
    private notificationService: NotificationService,
    @Inject(MAT_DIALOG_DATA) public data: { equipmentId: number },
  ) {
    this.serviceRequestForm = new FormGroup({
      title: new FormControl('', Validators.required),
      description: new FormControl(''),
    });

    this.equipmentService.getEquipment(this.data.equipmentId).subscribe({
      next: (data: Equipment) => (this.equipment = data),
      error: () => this.notificationService.error('Wystąpił błąd'),
    });
  }

  onSubmit() {
    if (this.serviceRequestForm.valid) {
      const data = this.serviceRequestForm.value;
      data.equipmentId = this.equipment.id;

      this.serviceRequestService.createServiceRequest(data).subscribe({
        next: () => {
          this.notificationService.success('Utworzono nowe zgłoszenie');
          this.dialogRef.close();
          window.location.reload();
        },
        error: () => this.notificationService.error('Wystąpił błąd'),
      });
    }
  }
}
