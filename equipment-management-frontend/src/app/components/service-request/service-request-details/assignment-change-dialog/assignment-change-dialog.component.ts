import { Component, Inject, OnInit } from '@angular/core';
import {
  FormControl,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { NgForOf, NgIf } from '@angular/common';
import { DialogRef } from '@angular/cdk/dialog';
import { ServiceRequestService } from '../../../../core/services/service-request.service';
import { NotificationService } from '../../../../core/services/shared/notification.service';
import { UserService } from '../../../../core/services/user.service';
import { UserModel } from '../../../../core/models/user/user.model';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-assignment-change-dialog',
  standalone: true,
  imports: [
    MatButton,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    NgIf,
    FormsModule,
    ReactiveFormsModule,
    MatSelectModule,
    NgForOf,
  ],
  templateUrl: './assignment-change-dialog.component.html',
  styleUrl: './assignment-change-dialog.component.scss',
})
export class AssignmentChangeDialogComponent implements OnInit {
  userList!: UserModel[];
  user: FormControl;

  constructor(
    private dialogRef: DialogRef<AssignmentChangeDialogComponent>,
    private serviceRequestService: ServiceRequestService,
    private userService: UserService,
    private notificationService: NotificationService,
    @Inject(MAT_DIALOG_DATA)
    public data: { serviceRequestId: number },
  ) {
    this.user = new FormControl(null, Validators.required);
  }

  ngOnInit(): void {
    this.userService.getActiveTechniciansUsers().subscribe({
      next: (data) => (this.userList = data),
      error: () => this.notificationService.showError('Wystąpił błąd'),
    });
  }

  onSubmit() {
    if (this.user.valid) {
      const data = this.user.value;

      this.serviceRequestService
        .assignTechnician(this.data.serviceRequestId, data)
        .subscribe({
          next: () => {
            this.notificationService.showSuccess(
              'Użytkownik został przypisany do zgłoszenia',
            );
            this.dialogRef.close();
            window.location.reload();
          },
          error: (err) =>
            this.notificationService.showError(
              err.error.message ? err.error.message : 'Wystąpił błąd',
            ),
        });
    }
  }
}
