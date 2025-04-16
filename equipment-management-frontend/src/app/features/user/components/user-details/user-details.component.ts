import { Component, OnInit } from '@angular/core';
import { DatePipe, Location, NgClass, NgForOf, NgIf } from '@angular/common';
import { MatButton } from '@angular/material/button';
import { ActivatedRoute, Router } from '@angular/router';
import { UserModel } from '../../models/user.model';
import { UserService } from '../../services/user.service';
import { ChangePasswordDialogComponent } from '../change-password-dialog/change-password-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { NotificationService } from '../../../../core/notification/services/notification.service';

@Component({
  selector: 'app-user-details',
  standalone: true,
  imports: [DatePipe, MatButton, NgClass, NgForOf, NgIf],
  templateUrl: './user-details.component.html',
  styleUrl: './user-details.component.scss',
})
export class UserDetailsComponent implements OnInit {
  user!: UserModel;

  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private notificationService: NotificationService,
    private location: Location,
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.userService.getUser(id).subscribe({
      next: (data) => {
        this.user = data;
      },
      error: () => this.notificationService.error('Wystąpił błąd'),
    });
  }

  goBack() {
    this.location.back();
  }

  editUser() {
    this.router.navigate(['users/edit', this.user.id]);
  }

  toggleActive() {
    const action = this.user.active ? 'deaktywować' : 'aktywować';
    const confirmed = confirm(
      'Czy na pewno chcesz ' +
        action +
        ' konto użytkownika ' +
        this.user.username +
        '?',
    );

    if (confirmed) {
      this.userService.toggleActive(this.user.id).subscribe({
        next: () => window.location.reload(),
        error: () => this.notificationService.error('Wystąpił błąd'),
      });
    }
  }

  changePassword() {
    this.dialog.open(ChangePasswordDialogComponent, {
      width: '500px',
      data: {
        userId: this.user.id,
      },
    });
  }

  getStatusClass(active: boolean) {
    return this.userService.getStatusClass(active);
  }
}
