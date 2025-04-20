import { Component, OnInit, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { User } from '../../models/user.model';
import { UserService } from '../../services/user.service';
import { MatSort, MatSortModule } from '@angular/material/sort';
import {
  MatPaginator,
  MatPaginatorModule,
  PageEvent,
} from '@angular/material/paginator';
import { Router, RouterLink } from '@angular/router';
import { NgClass } from '@angular/common';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import {RoleListDisplayPipe} from '../../../../shared/pipes/role-list-display.pipe';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    MatButtonModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    NgClass,
    RouterLink,
    RoleListDisplayPipe,
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss',
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  length = 0;
  pageSize = 10;
  pageIndex = 0;
  sort = 'id,desc';
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

  @ViewChild(MatSort) matSort!: MatSort;
  @ViewChild(MatPaginator) matPaginator!: MatPaginator;

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
      .getAllUsers(this.pageIndex, this.pageSize, this.sort)
      .subscribe({
        next: (data) => {
          this.users = data.content;
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
    this.router.navigate([this.router.url, id]);
  }

  getStatusClass(active: boolean) {
    return this.userService.getStatusClass(active);
  }
}
