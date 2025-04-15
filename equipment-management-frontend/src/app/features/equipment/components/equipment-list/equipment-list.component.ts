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
import { NgClass, NgIf } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { EquipmentStatusDisplayPipe } from '../../../../shared/pipes/equipment-status-display.pipe';
import { Equipment } from '../../models/equipment.model';

@Component({
  selector: 'app-equipment-list',
  standalone: true,
  imports: [
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatButtonModule,
    NgIf,
    RouterLink,
    EquipmentStatusDisplayPipe,
    NgClass,
  ],
  templateUrl: './equipment-list.component.html',
  styleUrl: './equipment-list.component.scss',
})
export class EquipmentListComponent implements OnInit {
  showAllEquipment = false;
  equipment: Equipment[] = [];
  length = 0;
  pageSize = 10;
  pageIndex = 0;
  sortField = 'id';
  sortOrder = 'desc';
  displayedColumns: string[] = [];

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private equipmentService: EquipmentService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.showAllEquipment = this.route.snapshot.data['showAllEquipment'];
    this.setDisplayedColumns();
    this.loadData();
  }

  loadData() {
    const equipmentObservable = this.showAllEquipment
      ? this.equipmentService.getAllEquipment(
          this.pageIndex,
          this.pageSize,
          this.sortField,
          this.sortOrder,
        )
      : this.equipmentService.getCurrentUserEquipment(
          this.pageIndex,
          this.pageSize,
          this.sortField,
          this.sortOrder,
        );

    equipmentObservable.subscribe({
      next: (data) => {
        this.equipment = data.content;
        this.length = data.totalElements;
      },
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
    switch (status) {
      case 'NEW':
        return 'status-badge status-new';
      case 'IN_PREPARATION':
        return 'status-badge status-in-preparation';
      case 'IN_USE':
        return 'status-badge status-in-use';
      case 'IN_REPAIR':
        return 'status-badge status-in-repair';
      case 'RESERVE':
        return 'status-badge status-reserve';
      case 'DECOMMISSIONED':
        return 'status-badge status-decommissioned';
      default:
        return 'status-badge';
    }
  }
}
