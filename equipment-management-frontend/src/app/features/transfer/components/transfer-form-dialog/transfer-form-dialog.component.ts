import { Component, Inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatOption } from '@angular/material/core';
import { MatSelect } from '@angular/material/select';
import { User } from '../../../user/models/user.model';
import { UserService } from '../../../user/services/user.service';
import { TransferService } from '../../services/transfer.service';
import { NgForOf } from '@angular/common';

@Component({
  selector: 'app-transfer-form-dialog',
  standalone: true,
  imports: [
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatOption,
    MatSelect,
    NgForOf,
  ],
  templateUrl: './transfer-form-dialog.component.html',
  styleUrl: './transfer-form-dialog.component.scss',
})
export class TransferFormDialogComponent implements OnInit {
  transferForm: FormGroup;
  userList!: User[];

  constructor(
    private dialogRef: MatDialogRef<TransferFormDialogComponent>,
    private transferService: TransferService,
    private userService: UserService,
    private notificationService: NotificationService,
    @Inject(MAT_DIALOG_DATA) public data: { equipmentId: number },
  ) {
    this.transferForm = new FormGroup({
      obtainerId: new FormControl(null, Validators.required),
    });
  }

  ngOnInit(): void {
    this.userService.getActiveUsers().subscribe({
      next: (data) => (this.userList = data),
      error: () => this.notificationService.error(),
    });
  }

  onSubmit() {
    if (this.transferForm.valid) {
      const data = this.transferForm.value;
      data.equipmentId = this.data.equipmentId;

      this.transferService.createTransfer(data).subscribe({
        next: () => {
          this.notificationService.success('Transfer has been created');
          this.dialogRef.close();
        },
        error: () => this.notificationService.error(),
      });
    }
  }
}
