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
import { RoleListDisplayPipe } from '../../../../shared/pipes/role-list-display.pipe';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatFormField, MatInput, MatLabel } from '@angular/material/input';
import { MatOption, MatSelect } from '@angular/material/select';
import { RoleDisplayPipe } from '../../../../shared/pipes/role-display.pipe';
import { Role } from '../../models/role.model';
import { RoleService } from '../../services/role.service';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { UserFilter } from '../../models/user-filter.model';

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
    FormsModule,
    MatFormField,
    MatSelect,
    MatOption,
    RoleDisplayPipe,
    ReactiveFormsModule,
    MatInput,
    MatLabel,
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss',
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  roles: Role[] = [];
  length = 0;
  pageSize = 10;
  pageIndex = 0;
  sort = 'id,desc';
  filterForm!: FormGroup;
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
    private roleService: RoleService,
    private router: Router,
    private notificationService: NotificationService,
  ) {}

  ngOnInit(): void {
    this.roleService.getAllRoles().subscribe({
      next: (data) => (this.roles = data),
      error: () => this.notificationService.error(),
    });

    this.loadData();

    this.filterForm = new FormGroup<UserFilter>({
      id: new FormControl(null),
      firstName: new FormControl(''),
      lastName: new FormControl(''),
      username: new FormControl(''),
      email: new FormControl(''),
      phoneNumber: new FormControl(''),
      roles: new FormControl(null),
      active: new FormControl(null),
    });

    this.filterForm.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe({
        next: (filters: UserFilter) => {
          this.pageIndex = 0;
          this.loadData(filters);
        },
      });
  }

  loadData(filters?: UserFilter) {
    this.userService
      .getAllUsers(this.pageIndex, this.pageSize, this.sort, filters)
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

  clearFilters() {
    this.filterForm.reset();
  }

  getStatusClass(active: boolean) {
    return this.userService.getStatusClass(active);
  }
}
