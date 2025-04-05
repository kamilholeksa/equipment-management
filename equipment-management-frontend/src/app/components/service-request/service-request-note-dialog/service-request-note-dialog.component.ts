import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { ServiceRequestNoteModel } from '../../../core/models/service-request-note/service-request-note.model';
import { MatButtonModule } from '@angular/material/button';
import { DatePipe, NgIf } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { ServiceRequestNoteService } from '../../../core/services/service-request-note.service';
import { NotificationService } from '../../../core/services/shared/notification.service';

@Component({
  selector: 'app-service-request-note-dialog',
  standalone: true,
  imports: [
    MatButtonModule,
    MatDialogModule,
    MatInputModule,
    DatePipe,
    NgIf,
    ReactiveFormsModule,
  ],
  templateUrl: './service-request-note-dialog.component.html',
  styleUrl: './service-request-note-dialog.component.scss',
})
export class ServiceRequestNoteDialogComponent {
  note!: ServiceRequestNoteModel;
  isOwner = false;
  noteForm: FormGroup;

  constructor(
    private noteService: ServiceRequestNoteService,
    private authService: AuthService,
    private notificationService: NotificationService,
    @Inject(MAT_DIALOG_DATA)
    public data: { note: ServiceRequestNoteModel },
  ) {
    this.note = data.note;

    if (this.note.createdBy === this.authService.userData.username) {
      this.isOwner = true;
    }

    this.noteForm = new FormGroup({
      description: new FormControl('', Validators.required),
      serviceRequestId: new FormControl(null, Validators.required),
    });

    this.populateForm(this.note);
  }

  updateNote() {
    if (this.noteForm.valid) {
      const data = this.noteForm.value;

      this.noteService.updateServiceRequestNote(this.note.id, data).subscribe({
        next: () => {
          this.notificationService.showSuccess('Notatka została zapisana');
          window.location.reload();
        },
        error: () => this.notificationService.showError('Wystąpił błąd'),
      });
    }
  }

  deleteNote() {
    const confirmed = confirm('Czy na pewno chcesz usunąć notatkę?');

    if (confirmed) {
      this.noteService.deleteNote(this.note.id).subscribe({
        next: () => {
          this.notificationService.showSuccess('Notatka została usunięta');
          window.location.reload();
        },
        error: () => this.notificationService.showError('Wystąpił błąd'),
      });
    }
  }

  private populateForm(note: ServiceRequestNoteModel) {
    this.noteForm.patchValue({
      description: note.description,
      serviceRequestId: note.serviceRequest.id,
    });
  }
}
