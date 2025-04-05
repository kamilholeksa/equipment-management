import { Component, OnInit, ViewChild } from '@angular/core';
import { ServiceRequestWithNotesModel } from '../../../core/models/service-request/service-request.model';
import { ServiceRequestService } from '../../../core/services/service-request.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
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
import { DatePipe, Location, NgForOf, NgIf } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { ServiceRequestNoteListComponent } from '../service-request-note-list/service-request-note-list.component';
import { ServiceRequestNoteService } from '../../../core/services/service-request-note.service';
import { NotificationService } from '../../../core/services/shared/notification.service';
import { TransferStatusDisplayPipe } from '../../../core/pipes/service-request-status-display.pipe';
import { MatDialog } from '@angular/material/dialog';
import { AssignmentChangeDialogComponent } from './assignment-change-dialog/assignment-change-dialog.component';
import { ServiceRequestCloseDialogComponent } from './service-request-close-dialog/service-request-close-dialog.component';
import { ServiceRequestStatusEnum } from '../../../core/enums/service-request-status.enum';

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
    NgForOf,
    DatePipe,
    ServiceRequestNoteListComponent,
    TransferStatusDisplayPipe,
    RouterLink,
    NgIf,
  ],
  templateUrl: './service-request-details.component.html',
  styleUrl: './service-request-details.component.scss',
})
export class ServiceRequestDetailsComponent implements OnInit {
  serviceRequest!: ServiceRequestWithNotesModel;
  serviceRequestForm!: FormGroup;
  noteForm!: FormGroup;

  protected readonly ServiceRequestStatusEnum = ServiceRequestStatusEnum;

  @ViewChild(ServiceRequestNoteListComponent)
  noteListComponent!: ServiceRequestNoteListComponent;

  constructor(
    private serviceRequestService: ServiceRequestService,
    private noteService: ServiceRequestNoteService,
    private notificationService: NotificationService,
    private router: Router,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private location: Location,
  ) {
    this.serviceRequestForm = new FormGroup({
      title: new FormControl('', Validators.required),
      description: new FormControl(''),
      equipmentId: new FormControl(null, Validators.required),
      userId: new FormControl(null, Validators.required),
      status: new FormControl(null, Validators.required),
      technicianId: new FormControl(null),
    });

    this.noteForm = new FormGroup({
      description: new FormControl('', Validators.required),
    });
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.serviceRequestService.getServiceRequest(id).subscribe({
      next: (data) => {
        this.serviceRequest = data;
        this.populateForm(data);
      },
      error: () => this.notificationService.showError('Wystąpił błąd'),
    });
  }

  populateForm(serviceRequest: ServiceRequestWithNotesModel): void {
    this.serviceRequestForm.patchValue({
      title: serviceRequest.title,
      description: serviceRequest.description,
      equipment: serviceRequest.equipment?.id,
      user: serviceRequest.user?.id,
      status: serviceRequest.status,
      technician: serviceRequest.technician?.id,
    });
  }

  saveNote() {
    if (this.noteForm.valid) {
      const data = this.noteForm.value;
      data.serviceRequestId = this.serviceRequest.id;

      this.noteService.createServiceRequestNote(data).subscribe({
        next: () => {
          this.notificationService.showSuccess('Dodano notatkę');
          this.noteListComponent.loadData();
          this.noteForm.reset();
        },
        error: (err) =>
          this.notificationService.showError(
            err.error.message ? err.error.message : 'Wystąpił błąd',
          ),
      });
    }

    this.noteForm.reset();
  }

  accept() {
    this.serviceRequestService.accept(this.serviceRequest.id).subscribe({
      next: (result) => {
        this.notificationService.showSuccess(result.message);
        window.location.reload();
      },
      error: (err) =>
        this.notificationService.showError(
          err.error.message ? err.error.message : 'Wystąpił błąd',
        ),
    });
  }

  changeAssignment() {
    this.dialog.open(AssignmentChangeDialogComponent, {
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
}
