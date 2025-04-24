import { Component, OnInit, ViewChild } from '@angular/core';
import { ServiceRequestWithNotesModel } from '../../models/service-request.model';
import { ServiceRequestService } from '../../services/service-request.service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { DatePipe, Location, NgClass } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { ServiceRequestNoteListComponent } from '../service-request-note-list/service-request-note-list.component';
import { ServiceRequestNoteService } from '../../services/service-request-note.service';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import { MatDialog } from '@angular/material/dialog';
import { ServiceRequestAssignDialogComponent } from '../service-request-assign-dialog/service-request-assign-dialog.component';
import { ServiceRequestCloseDialogComponent } from '../service-request-close-dialog/service-request-close-dialog.component';
import { ServiceRequestStatusEnum } from '../../../../shared/enums/service-request-status.enum';
import { ServiceRequestStatusDisplayPipe } from '../../../../shared/pipes/service-request-status-display.pipe';
import { ServiceRequestNoteSave } from '../../models/service-request-note-save.model';

@Component({
  selector: 'app-service-request-details',
  standalone: true,
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatTableModule,
    ServiceRequestNoteListComponent,
    RouterLink,
    NgClass,
    DatePipe,
    ServiceRequestStatusDisplayPipe,
  ],
  templateUrl: './service-request-details.component.html',
  styleUrl: './service-request-details.component.scss',
})
export class ServiceRequestDetailsComponent implements OnInit {
  serviceRequest!: ServiceRequestWithNotesModel;
  note: FormControl;

  protected readonly ServiceRequestStatusEnum = ServiceRequestStatusEnum;

  @ViewChild(ServiceRequestNoteListComponent)
  noteListComponent!: ServiceRequestNoteListComponent;

  constructor(
    private serviceRequestService: ServiceRequestService,
    private noteService: ServiceRequestNoteService,
    private notificationService: NotificationService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private location: Location,
  ) {
    this.note = new FormControl('', Validators.required);
  }

  ngOnInit(): void {
    this.route.data.subscribe({
      next: ({ serviceRequest }) => {
        this.serviceRequest = serviceRequest;
      },
      error: () => this.notificationService.error(),
    });
  }

  loadData() {
    this.serviceRequestService.getServiceRequest(this.serviceRequest.id).subscribe({
      next: (data) => {
        this.serviceRequest = data;
      },
      error: () => this.notificationService.error(),
    });
  }

  saveNote() {
    if (this.note.valid) {
      const data: ServiceRequestNoteSave = {
        description: this.note.value,
        serviceRequestId: this.serviceRequest.id,
      };

      this.noteService.createServiceRequestNote(data).subscribe({
        next: () => {
          this.noteListComponent.loadData();
        },
        error: (err) => this.notificationService.error(err.error.message),
      });
    }

    this.note.reset();
  }

  accept() {
    this.serviceRequestService.accept(this.serviceRequest.id).subscribe({
      next: (result) => {
        this.notificationService.success(result.message);
        this.loadData();
      },
      error: (err) => this.notificationService.error(err.error.message),
    });
  }

  changeAssignment() {
    const dialogRef = this.dialog.open(ServiceRequestAssignDialogComponent, {
      width: '800px',
      data: {
        serviceRequestId: this.serviceRequest.id,
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  close() {
    const dialogRef = this.dialog.open(ServiceRequestCloseDialogComponent, {
      width: '800px',
      data: {
        serviceRequestId: this.serviceRequest.id,
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  goBack() {
    this.location.back();
  }

  getStatusClass(status: string): string {
    return this.serviceRequestService.getStatusClass(status);
  }
}
