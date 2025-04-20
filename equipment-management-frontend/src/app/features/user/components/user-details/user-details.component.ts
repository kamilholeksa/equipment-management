import { Component, OnInit } from '@angular/core';
import { DatePipe, Location, NgClass, NgForOf } from '@angular/common';
import { MatButton } from '@angular/material/button';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../models/user.model';
import { UserService } from '../../services/user.service';
import { ChangePasswordDialogComponent } from '../change-password-dialog/change-password-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import {RoleListDisplayPipe} from '../../../../shared/pipes/role-list-display.pipe';

@Component({
  selector: 'app-user-details',
  standalone: true,
  imports: [DatePipe, MatButton, NgClass, NgForOf, RoleListDisplayPipe],
  templateUrl: './user-details.component.html',
  styleUrl: './user-details.component.scss',
})
export class UserDetailsComponent implements OnInit {
  user!: User;

  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private notificationService: NotificationService,
    private location: Location,
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe({
      next: ({ user }) => (this.user = user),
      error: () => this.notificationService.error(),
    });
  }

  goBack() {
    this.location.back();
  }

  editUser() {
    this.router.navigate(['users/edit', this.user.id]);
  }

  toggleActive() {
    const action = this.user.active ? 'deactivate' : 'activate';
    const confirmed = confirm(
      'Are you sure you want to ' +
        action +
        ' user ' +
        this.user.username +
        '?',
    );

    if (confirmed) {
      this.userService.toggleActive(this.user.id).subscribe({
        next: () => window.location.reload(),
        error: () => this.notificationService.error(),
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
