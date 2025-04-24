import { Component, OnInit, ViewChild } from '@angular/core';
import { EquipmentService } from '../../services/equipment.service';
import { MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule } from '@angular/material/sort';
import {
  MatPaginator,
  MatPaginatorModule,
  PageEvent,
} from '@angular/material/paginator';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { NgClass } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { EquipmentStatusDisplayPipe } from '../../../../shared/pipes/equipment-status-display.pipe';
import { Equipment } from '../../models/equipment.model';
import { MatInputModule } from '@angular/material/input';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatOption } from '@angular/material/core';
import { MatSelect } from '@angular/material/select';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { EquipmentFilter } from '../../models/equipment-filter.model';
import { UserService } from '../../../user/services/user.service';
import { User } from '../../../user/models/user.model';
import { EquipmentStatusEnum } from '../../../../shared/enums/equipment-status.enum';

@Component({
  selector: 'app-equipment-list',
  standalone: true,
  imports: [
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatButtonModule,
    RouterLink,
    EquipmentStatusDisplayPipe,
    NgClass,
    MatInputModule,
    ReactiveFormsModule,
    MatSelect,
    MatOption,
  ],
  templateUrl: './equipment-list.component.html',
  styleUrl: './equipment-list.component.scss',
})
export class EquipmentListComponent implements OnInit {
  showAllEquipment = false;
  statuses: EquipmentStatusEnum[] = Object.values(EquipmentStatusEnum);
  users: User[] = [];
  equipment: Equipment[] = [];
  length = 0;
  pageSize = 10;
  pageIndex = 0;
  sort = 'id,desc';
  displayedColumns: string[] = [];
  filterForm!: FormGroup;

  @ViewChild(MatSort) matSort!: MatSort;
  @ViewChild(MatPaginator) matPaginator!: MatPaginator;

  constructor(
    private equipmentService: EquipmentService,
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.showAllEquipment = this.route.snapshot.data['showAllEquipment'];

    if (this.showAllEquipment) {
      this.userService.getActiveUsers().subscribe({
        next: (data) => (this.users = data),
      });
    }

    this.setDisplayedColumns();
    this.loadData();

    this.filterForm = new FormGroup<EquipmentFilter>({
      manufacturer: new FormControl(''),
      model: new FormControl(''),
      inventoryNumber: new FormControl(''),
      serialNumber: new FormControl(''),
      status: new FormControl(null),
      userId: new FormControl(null),
    });

    this.filterForm.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe({
        next: (filters: EquipmentFilter) => {
          this.pageIndex = 0;
          this.loadData(filters);
        },
      });
  }

  loadData(filters?: EquipmentFilter) {
    const equipmentObservable = this.showAllEquipment
      ? this.equipmentService.getAllEquipment(
          this.pageIndex,
          this.pageSize,
          this.sort,
          filters,
        )
      : this.equipmentService.getCurrentUserEquipment(
          this.pageIndex,
          this.pageSize,
          this.sort,
          filters,
        );

    equipmentObservable.subscribe({
      next: (data) => {
        this.equipment = data.content;
        this.length = data.page.totalElements;
      },
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

  private setDisplayedColumns() {
    if (this.showAllEquipment) {
      this.displayedColumns = [
        'manufacturer',
        'model',
        'inventoryNumber',
        'serialNumber',
        'status',
        'user',
        'purchaseDate',
        'warrantyUntil',
        'withdrawalDate',
      ];
    } else {
      this.displayedColumns = [
        'manufacturer',
        'model',
        'inventoryNumber',
        'serialNumber',
        'status',
        'purchaseDate',
        'warrantyUntil',
      ];
    }
  }

  getStatusClass(status: string): string {
    return this.equipmentService.getStatusClass(status);
  }
}
