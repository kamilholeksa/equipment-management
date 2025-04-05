import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { ServiceRequestService } from '../../../core/services/service-request.service';
import { ServiceRequestModel } from '../../../core/models/service-request/service-request.model';
import { MatSort, MatSortModule } from '@angular/material/sort';
import {
  MatPaginator,
  MatPaginatorModule,
  PageEvent,
} from '@angular/material/paginator';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { DatePipe, Location, NgIf } from '@angular/common';
import { TransferStatusDisplayPipe } from '../../../core/pipes/service-request-status-display.pipe';
import { MatDialog } from '@angular/material/dialog';
import { ServiceRequestDetailsDialogComponent } from '../service-request-details-dialog/service-request-details-dialog.component';
import { ServiceRequestFormDialogComponent } from '../service-request-form-dialog/service-request-form-dialog.component';
import { NotificationService } from '../../../core/services/shared/notification.service';

@Component({
  selector: 'app-service-request-list',
  standalone: true,
  imports: [
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatButtonModule,
    NgIf,
    RouterLink,
    DatePipe,
    TransferStatusDisplayPipe,
  ],
  templateUrl: './service-request-list.component.html',
  styleUrl: './service-request-list.component.scss',
})
export class ServiceRequestListComponent implements OnInit {
  serviceRequests: ServiceRequestModel[] = [];
  length = 0;
  pageSize = 10;
  pageIndex = 0;
  sortField = 'id';
  sortOrder = 'desc';
  showAll = false;
  equipmentId?: number;
  displayedColumns!: string[];

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private serviceRequestService: ServiceRequestService,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location,
    private dialog: MatDialog,
    private notificationService: NotificationService,
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.equipmentId = params['equipmentId']
        ? +params['equipmentId']
        : undefined;
      this.pageIndex = 0;
      this.setDisplayedColumns();
      this.loadData();
    });
  }

  loadData() {
    let observable;

    if (this.equipmentId) {
      observable = this.serviceRequestService.getServiceRequestsForEquipment(
        this.equipmentId,
        this.pageIndex,
        this.pageSize,
        this.sortField,
        this.sortOrder,
      );
    } else {
      if (this.showAll) {
        observable = this.serviceRequestService.getAllServiceRequests(
          this.pageIndex,
          this.pageSize,
          this.sortField,
          this.sortOrder,
        );
      } else {
        observable = this.serviceRequestService.getOpenServiceRequests(
          this.pageIndex,
          this.pageSize,
          this.sortField,
          this.sortOrder,
        );
      }
    }

    observable.subscribe({
      next: (data) => {
        this.serviceRequests = data.content;
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

  onSortChange() {
    const sortField = this.sort.active;
    const sortOrder = this.sort.direction;

    if (sortField && sortOrder) {
      this.sortField = sortField;
      this.sortOrder = sortOrder === 'asc' ? 'asc' : 'desc';
    } else {
      this.sortField = 'id';
      this.sortOrder = 'desc';
    }
    this.loadData();
  }

  goToDetails(id: number): void {
    if (!this.equipmentId) {
      this.router.navigate([this.router.url, id]);
    } else {
      this.dialog.open(ServiceRequestDetailsDialogComponent, {
        width: '800px',
        maxWidth: '100%',
        data: {
          serviceRequest: this.serviceRequests.find((item) => item.id === id),
        },
      });
    }
  }

  openServiceRequestForm() {
    this.dialog.open(ServiceRequestFormDialogComponent, {
      width: '800px',
      maxWidth: '100%',
      data: {
        equipmentId: this.equipmentId,
      },
    });
  }

  toggleShowAll() {
    this.showAll = !this.showAll;
    this.pageIndex = 0;
    this.loadData();
  }

  goBack() {
    this.location.back();
  }

  private setDisplayedColumns() {
    if (this.equipmentId) {
      this.displayedColumns = ['id', 'title', 'user', 'status', 'createdDate'];
    } else {
      this.displayedColumns = [
        'id',
        'title',
        'user',
        'technician',
        'status',
        'createdDate',
      ];
    }
  }
}
