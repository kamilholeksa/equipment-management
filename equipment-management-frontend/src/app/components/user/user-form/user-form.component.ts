import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { UserService } from '../../../core/services/user.service';
import { RoleService } from '../../../core/services/role.service';
import { Role } from '../../../core/models/role/role.model';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Location, NgForOf, NgIf } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '../../../core/services/shared/notification.service';
import { UserModel } from '../../../core/models/user/user.model';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatSelectModule,
    MatDatepickerModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    NgForOf,
    NgIf,
  ],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.scss',
})
export class UserFormComponent implements OnInit {
  userForm: FormGroup;
  roles!: Role[];
  userId!: number;
  isEditMode = false;

  constructor(
    private userService: UserService,
    private roleService: RoleService,
    private router: Router,
    private route: ActivatedRoute,
    private notificationService: NotificationService,
    private location: Location,
  ) {
    this.userForm = new FormGroup({
      firstName: new FormControl('', Validators.required),
      lastName: new FormControl('', Validators.required),
      username: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      phoneNumber: new FormControl(''),
      roles: new FormControl([], Validators.required),
      active: new FormControl(null),
      password: new FormControl('', [
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
    this.roleService.getAllRoles().subscribe({
      next: (data) => (this.roles = data),
      error: () => this.notificationService.showError('Wystąpił błąd'),
    });

    this.userId = +this.route.snapshot.paramMap.get('id')!;
    if (this.userId) {
      this.isEditMode = true;
      this.userService.getUser(this.userId).subscribe({
        next: (user) => this.populateForm(user),
        error: () => this.notificationService.showError('Wystąpił błąd'),
      });
    }

    this.updateFormForEditMode();
  }

  updateFormForEditMode() {
    if (this.isEditMode) {
      this.userForm.removeControl('password');
      this.userForm.removeControl('confirmPassword');
    }
  }

  populateForm(user: UserModel) {
    this.userForm.patchValue({
      firstName: user.firstName,
      lastName: user.lastName,
      username: user.username,
      email: user.email,
      phoneNumber: user.phoneNumber,
      roles: user.roles,
      active: user.active,
    });
  }

  passwordMatchValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const newPassword = this.userForm?.get('password')?.value;
      const confirmPassword = control.value;
      return newPassword === confirmPassword
        ? null
        : { passwordMismatch: true };
    };
  }

  onSubmit() {
    if (this.userForm.valid) {
      const userData = this.userForm.value;
      userData.active = this.isEditMode ? userData.active : true;

      const observable = this.userId
        ? this.userService.updateUser(this.userId, {
            ...userData,
            password: undefined,
            confirmPassword: undefined,
          })
        : this.userService.createUser(userData);

      observable.subscribe({
        next: () => this.goBack(),
        error: (err) =>
          this.notificationService.showError(
            err.error.message ? err.error.message : 'Wystąpił błąd',
          ),
      });
    }
  }

  goBack() {
    this.location.back();
  }
}
