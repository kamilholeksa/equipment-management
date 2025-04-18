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
  sort = 'id,desc';
  displayedColumns: string[] = [];

  @ViewChild(MatSort) matSort!: MatSort;
  @ViewChild(MatPaginator) matPaginator!: MatPaginator;

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
          this.sort
        )
      : this.equipmentService.getCurrentUserEquipment(
          this.pageIndex,
          this.pageSize,
          this.sort
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
