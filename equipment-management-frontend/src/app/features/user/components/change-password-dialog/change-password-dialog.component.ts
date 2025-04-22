import { Component, Inject, OnInit } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { UserService } from '../../services/user.service';
import { NotificationService } from '../../../../core/notification/services/notification.service';

@Component({
  selector: 'app-change-password-dialog',
  standalone: true,
  imports: [
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './change-password-dialog.component.html',
  styleUrl: './change-password-dialog.component.scss',
})
export class ChangePasswordDialogComponent implements OnInit {
  changePasswordForm: FormGroup;
  currentUser = true;

  constructor(
    private dialogRef: MatDialogRef<ChangePasswordDialogComponent>,
    private userService: UserService,
    private notificationService: NotificationService,
    @Inject(MAT_DIALOG_DATA) public data: { userId: number },
  ) {
    this.changePasswordForm = new FormGroup({
      currentPassword: new FormControl('', Validators.required),
      newPassword: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
      ]),
      confirmPassword: new FormControl('', [
        Validators.required,
        this.passwordMatchValidator(),
      ]),
    });
  }

  ngOnInit(): void {
    if (this.data?.userId) {
      this.currentUser = false;
      this.changePasswordForm.removeControl('currentPassword');
    }
  }

  passwordMatchValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const newPassword = this.changePasswordForm?.get('newPassword')?.value;
      const confirmPassword = control.value;
      return newPassword === confirmPassword
        ? null
        : { passwordMismatch: true };
    };
  }

  onSubmit() {
    if (this.changePasswordForm.valid) {
      const data = this.changePasswordForm.value;

      const observable = this.currentUser
        ? this.userService.changeCurrentUserPassword(data)
        : this.userService.changePassword(this.data.userId, data);

      observable.subscribe({
        next: () => {
          this.notificationService.success('Password has been changed');
          this.dialogRef.close();
        },
        error: (err) => this.notificationService.error(err.error.message),
      });
    }
  }
}
