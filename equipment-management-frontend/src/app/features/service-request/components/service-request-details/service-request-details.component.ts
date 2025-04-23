import { Component, OnInit, ViewChild } from '@angular/core';
import { ServiceRequestWithNotesModel } from '../../models/service-request.model';
import { ServiceRequestService } from '../../services/service-request.service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
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
import {ServiceRequestStatusDisplayPipe} from '../../../../shared/pipes/service-request-status-display.pipe';

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
  noteForm!: FormGroup; //TODO: Change to form control

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
    this.noteForm = new FormGroup({
      description: new FormControl('', Validators.required),
    });
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.serviceRequestService.getServiceRequest(id).subscribe({
      next: (data) => {
        this.serviceRequest = data;
      },
      error: () => this.notificationService.error(),
    });

    this.route.data.subscribe({
      next: ({ serviceRequest }) => {
        this.serviceRequest = serviceRequest;
      },
      error: () => this.notificationService.error(),
    });
  }

  saveNote() {
    if (this.noteForm.valid) {
      const data = this.noteForm.value;
      data.serviceRequestId = this.serviceRequest.id;

      this.noteService.createServiceRequestNote(data).subscribe({
        next: () => {
          this.noteListComponent.loadData();
          this.noteForm.reset();
        },
        error: (err) => this.notificationService.error(err.error.message),
      });
    }

    this.noteForm.reset();
  }

  accept() {
    this.serviceRequestService.accept(this.serviceRequest.id).subscribe({
      next: (result) => {
        this.notificationService.success(result.message);
        window.location.reload();
      },
      error: (err) => this.notificationService.error(err.error.message),
    });
  }

  changeAssignment() {
    this.dialog.open(ServiceRequestAssignDialogComponent, {
      width: '800px',
      data: {
        serviceRequestId: this.serviceRequest.id,
      },
    });
  }

  close() {
    this.dialog.open(ServiceRequestCloseDialogComponent, {
      width: '800px',
      data: {
        serviceRequestId: this.serviceRequest.id,
      },
    });
  }

  goBack() {
    this.location.back();
  }

  getStatusClass(status: string): string {
    return this.serviceRequestService.getStatusClass(status);
  }
}
