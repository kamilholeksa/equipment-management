import { Component, OnInit, ViewChild } from '@angular/core';
import { EquipmentStatusDisplayPipe } from '../../../core/pipes/equipment-status-display.pipe';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { UserModel } from '../../../core/models/user/user.model';
import { UserService } from '../../../core/services/user.service';
import { MatSort, MatSortModule } from '@angular/material/sort';
import {
  MatPaginator,
  MatPaginatorModule,
  PageEvent,
} from '@angular/material/paginator';
import { Router, RouterLink } from '@angular/router';
import { NgClass, NgForOf, NgIf } from '@angular/common';
import { NotificationService } from '../../../core/services/shared/notification.service';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    EquipmentStatusDisplayPipe,
    MatButtonModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    NgForOf,
    NgIf,
    NgClass,
    RouterLink,
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss',
})
export class UserListComponent implements OnInit {
  users: UserModel[] = [];
  length = 0;
  pageSize = 10;
  pageIndex = 0;
  sortField = 'id';
  sortOrder = 'desc';
  displayedColumns = [
    'id',
    'firstName',
    'lastName',
    'username',
    'email',
    'phoneNumber',
    'roles',
    'active',
  ];

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private userService: UserService,
    private router: Router,
    private notificationService: NotificationService,
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.userService
      .getAllUsers(
        this.pageIndex,
        this.pageSize,
        this.sortField,
        this.sortOrder,
      )
      .subscribe({
        next: (data) => {
          this.users = data.content;
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
    this.router.navigate([this.router.url, id]);
  }
}
