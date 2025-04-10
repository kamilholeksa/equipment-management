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
import { AuthService } from '../../../core/services/auth/auth.service';
import { NotificationService } from '../../../core/services/shared/notification.service';
import { AuthResponse } from '../../../core/models/auth/auth-response.model';
import { AccountModel } from '../../../core/models/auth/account.model';
import { Router } from '@angular/router';
import { TokenStorageService } from '../../../core/services/auth/token-storage.service';

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
    private notificationService: NotificationService,
    private router: Router,
  ) {}

  submit(): void {
    this.submitted = true;
    const formData = this.loginForm.value;

    this.authService.login(formData.username, formData.password).subscribe({
      next: () => {
        this.router.navigate(['/']);
        this.notificationService.showSuccess(
          'Zalogowano pomyślnie',
        )},
      error: (err) =>
        this.notificationService.showError(
          err?.error?.message || 'Niepoprawne hasło',
        ),
    });
  }
}
