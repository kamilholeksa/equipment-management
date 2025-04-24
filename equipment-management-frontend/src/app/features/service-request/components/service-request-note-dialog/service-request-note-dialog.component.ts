import { Component, Inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { ServiceRequestNote } from '../../models/service-request-note.model';
import { MatButtonModule } from '@angular/material/button';
import { DatePipe } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../../core/auth/services/auth.service';
import { ServiceRequestNoteService } from '../../services/service-request-note.service';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import { ServiceRequestNoteSave } from '../../models/service-request-note-save.model';

@Component({
  selector: 'app-service-request-note-dialog',
  standalone: true,
  imports: [
    MatButtonModule,
    MatDialogModule,
    MatInputModule,
    DatePipe,
    ReactiveFormsModule,
  ],
  templateUrl: './service-request-note-dialog.component.html',
  styleUrl: './service-request-note-dialog.component.scss',
})
export class ServiceRequestNoteDialogComponent {
  note!: ServiceRequestNote;
  isOwner = false;
  noteControl: FormControl;

  constructor(
    private dialogRef: MatDialogRef<ServiceRequestNoteDialogComponent>,
    private noteService: ServiceRequestNoteService,
    private authService: AuthService,
    private notificationService: NotificationService,
    @Inject(MAT_DIALOG_DATA)
    public data: { note: ServiceRequestNote },
  ) {
    this.note = data.note;
    this.noteControl = new FormControl(this.note.description, [
      Validators.required,
      Validators.maxLength(255),
    ]);
    if (this.note.createdBy === this.authService.account()?.username) {
      this.isOwner = true;
    }
  }

  updateNote() {
    if (this.noteControl.valid) {
      const data: ServiceRequestNoteSave = {
        description: this.noteControl.value,
        serviceRequestId: this.note.serviceRequest.id,
      };

      this.noteService.updateServiceRequestNote(this.note.id, data).subscribe({
        next: () => {
          this.dialogRef.close(true);
        },
        error: () => this.notificationService.error(),
      });
    }
  }

  deleteNote() {
    const confirmed = confirm('Are you sure you want to delete the note?');

    if (confirmed) {
      this.noteService.deleteNote(this.note.id).subscribe({
        next: () => {
          this.dialogRef.close(true);
        },
        error: () => this.notificationService.error(),
      });
    }
  }
}
