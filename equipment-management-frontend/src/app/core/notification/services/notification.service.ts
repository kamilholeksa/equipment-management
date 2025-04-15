import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  constructor(private snackBar: MatSnackBar) {}

  success(message: string, action = 'OK', duration = 5000): void {
    this.snackBar.open(message, action, {
      verticalPosition: 'top',
      panelClass: ['app-notification-success'],
      duration,
    });
  }

  error(message: string, action = 'OK', duration = 5000): void {
    this.snackBar.open(message, action, {
      verticalPosition: 'top',
      panelClass: ['app-notification-error'],
      duration,
    });
  }
}
