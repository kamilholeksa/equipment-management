import { Component, Input, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { ServiceRequestNoteModel } from '../../../core/models/service-request-note/service-request-note.model';
import { DatePipe } from '@angular/common';
import { ServiceRequestNoteService } from '../../../core/services/service-request-note.service';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { ServiceRequestNoteDialogComponent } from '../service-request-note-dialog/service-request-note-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { NotificationService } from '../../../core/services/shared/notification.service';

@Component({
  selector: 'app-service-request-note-list',
  standalone: true,
  imports: [MatTableModule, DatePipe, MatPaginator],
  templateUrl: './service-request-note-list.component.html',
  styleUrl: './service-request-note-list.component.scss',
})
export class ServiceRequestNoteListComponent implements OnInit {
  @Input() serviceRequestId!: number;
  notes!: ServiceRequestNoteModel[];
  length = 0;
  pageSize = 10;
  pageIndex = 0;
  displayedColumns = ['description', 'createdBy', 'createdDate'];

  constructor(
    private noteService: ServiceRequestNoteService,
    private dialog: MatDialog,
    private notificationService: NotificationService,
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.noteService
      .getServiceRequestNotes(
        this.serviceRequestId,
        this.pageIndex,
        this.pageSize,
      )
      .subscribe({
        next: (data) => {
          this.notes = data.content;
          this.length = data.totalElements;
        },
        error: () => this.notificationService.showError('Wystąpił błąd'),
      });
  }

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData();
  }

  openNoteDialog(note: ServiceRequestNoteModel): void {
    this.dialog.open(ServiceRequestNoteDialogComponent, {
      width: '800px',
      maxWidth: '100%',
      data: {
        note: note,
      },
    });
  }
}
