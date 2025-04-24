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
import { MatDialog } from '@angular/material/dialog';
import { ServiceRequestDetailsDialogComponent } from '../service-request-details-dialog/service-request-details-dialog.component';
import { ServiceRequestFormDialogComponent } from '../service-request-form-dialog/service-request-form-dialog.component';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatFormField, MatInput, MatLabel } from '@angular/material/input';
import { MatSelect } from '@angular/material/select';
import { MatOption } from '@angular/material/core';
import { User } from '../../../user/models/user.model';
import { UserService } from '../../../user/services/user.service';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { ServiceRequestFilter } from '../../models/service-request-filter.model';
import { ServiceRequestStatusDisplayPipe } from '../../../../shared/pipes/service-request-status-display.pipe';
import { ServiceRequestStatusEnum } from '../../../../shared/enums/service-request-status.enum';

@Component({
  selector: 'app-service-request-list',
  standalone: true,
  imports: [
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatButtonModule,
    DatePipe,
    NgClass,
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    MatOption,
    MatSelect,
    ReactiveFormsModule,
    MatFormField,
    ServiceRequestStatusDisplayPipe,
  ],
  templateUrl: './service-request-list.component.html',
  styleUrl: './service-request-list.component.scss',
})
export class ServiceRequestListComponent implements OnInit {
  serviceRequests: ServiceRequest[] = [];
  statuses: ServiceRequestStatusEnum[] = Object.values(
    ServiceRequestStatusEnum,
  );
  users: User[] = [];
  technicians: User[] = [];
  length = 0;
  pageSize = 10;
  pageIndex = 0;
  sort = 'id,desc';
  showAll = false;
  equipmentId?: number;
  displayedColumns!: string[];
  filterForm!: FormGroup;

  @ViewChild(MatSort) matSort!: MatSort;
  @ViewChild(MatPaginator) matPaginator!: MatPaginator;

  constructor(
    private serviceRequestService: ServiceRequestService,
    private userService: UserService,
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

      this.setDisplayedColumns();
      this.loadData();

      if (!this.equipmentId) {
        this.userService.getActiveUsers().subscribe({
          next: (data) => {
            this.users = data;
            this.technicians = data.filter((user) =>
              user.roles.includes('ROLE_TECHNICIAN'),
            );
          },
        });

        this.filterForm = new FormGroup<ServiceRequestFilter>({
          id: new FormControl(null),
          title: new FormControl(''),
          status: new FormControl(null),
          userId: new FormControl(null),
          technicianId: new FormControl(null),
        });

        this.filterForm.valueChanges
          .pipe(debounceTime(300), distinctUntilChanged())
          .subscribe({
            next: (filters: ServiceRequestFilter) => {
              this.pageIndex = 0;
              this.loadData(filters);
            },
          });
      }
    });
  }

  loadData(filters?: ServiceRequestFilter) {
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
          filters,
        );
      } else {
        observable = this.serviceRequestService.getOpenServiceRequests(
          this.pageIndex,
          this.pageSize,
          this.sort,
          filters,
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
      const dialogRef = this.dialog.open(ServiceRequestDetailsDialogComponent, {
        width: '800px',
        maxWidth: '100%',
        data: {
          serviceRequest: this.serviceRequests.find((item) => item.id === id),
        },
      });

      dialogRef.afterClosed().subscribe((result) => {
        if (result) {
          this.loadData();
        }
      })
    }
  }

  openServiceRequestForm() {
    const dialogRef = this.dialog.open(ServiceRequestFormDialogComponent, {
      width: '800px',
      maxWidth: '100%',
      data: {
        equipmentId: this.equipmentId,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadData();
      }
    });
  }

  toggleShowAll() {
    this.showAll = !this.showAll;
    this.pageIndex = 0;
    this.loadData();
  }

  clearFilters() {
    this.filterForm.reset();
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
