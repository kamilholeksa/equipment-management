import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { NgIf } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/shared/notification.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatButtonModule,
    NgIf,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  loginForm: FormGroup = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });
  submitted = false;

  constructor(
    private authService: AuthService,
    private formBuilder: FormBuilder,
    private notificationService: NotificationService,
  ) {
    if (authService.isLoggedIn()) {
      window.location.href = '/';
    }
  }

  submit(): void {
    this.submitted = true;
    const formData = this.loginForm.value;

    this.authService
      .login({ username: formData.username, password: formData.password })
      .subscribe({
        next: (res) => {
          this.authService.setAuthToken(res.token);
          window.location.href = '/';
        },
        error: (err) =>
          this.notificationService.showError(
            err.error.message ? err.error.message : 'Wystąpił błąd',
          ),
      });
  }
}
