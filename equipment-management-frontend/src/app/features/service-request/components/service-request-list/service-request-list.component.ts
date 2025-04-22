import { Component, OnInit, ViewChild } from '@angular/core';
import { ServiceRequestService } from '../../services/service-request.service';
import { ServiceRequest } from '../../models/service-request.model';
import { MatSort, MatSortModule } from '@angular/material/sort';
import {
  MatPaginator,
  MatPaginatorModule,
  PageEvent,
} from '@angular/material/paginator';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { DatePipe, Location, NgClass } from '@angular/common';
import { TransferStatusDisplayPipe } from '../../../../shared/pipes/service-request-status-display.pipe';
import { MatDialog } from '@angular/material/dialog';
import { ServiceRequestDetailsDialogComponent } from '../service-request-details-dialog/service-request-details-dialog.component';
import { ServiceRequestFormDialogComponent } from '../service-request-form-dialog/service-request-form-dialog.component';
import { NotificationService } from '../../../../core/notification/services/notification.service';

@Component({
  selector: 'app-service-request-list',
  standalone: true,
  imports: [
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatButtonModule,
    DatePipe,
    TransferStatusDisplayPipe,
    NgClass,
  ],
  templateUrl: './service-request-list.component.html',
  styleUrl: './service-request-list.component.scss',
})
export class ServiceRequestListComponent implements OnInit {
  serviceRequests: ServiceRequest[] = [];
  length = 0;
  pageSize = 10;
  pageIndex = 0;
  sort = 'id,desc';
  showAll = false;
  equipmentId?: number;
  displayedColumns!: string[];

  @ViewChild(MatSort) matSort!: MatSort;
  @ViewChild(MatPaginator) matPaginator!: MatPaginator;

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
        this.sort,
      );
    } else {
      if (this.showAll) {
        observable = this.serviceRequestService.getAllServiceRequests(
          this.pageIndex,
          this.pageSize,
          this.sort,
        );
      } else {
        observable = this.serviceRequestService.getOpenServiceRequests(
          this.pageIndex,
          this.pageSize,
          this.sort,
        );
      }
    }

    observable.subscribe({
      next: (data) => {
        this.serviceRequests = data.content;
        this.length = data.page.totalElements;
      },
      error: () => this.notificationService.error(),
    });
  }

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData();
  }

  onSortChange() {
    const sortField = this.matSort.active;
    const sortOrder = this.matSort.direction;

    if (sortField && sortOrder) {
      this.sort = `${sortField},${sortOrder}`;
    } else {
      this.sort = 'id,desc';
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

  getStatusClass(status: string): string {
    return this.serviceRequestService.getStatusClass(status);
  }
}
