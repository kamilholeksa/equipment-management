import { Component, OnInit, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { EquipmentTypeModel } from '../../../core/models/equipment-type/equipment-type.model';
import { EquipmentTypeService } from '../../../core/services/equipment-type.service';
import { EquipmentTypeFormDialogComponent } from '../equipment-type-form-dialog/equipment-type-form-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { NotificationService } from '../../../core/services/shared/notification.service';

@Component({
  selector: 'app-equipment-type-list',
  standalone: true,
  imports: [MatButtonModule, MatTableModule, MatSortModule],
  templateUrl: './equipment-type-list.component.html',
  styleUrl: './equipment-type-list.component.scss',
})
export class EquipmentTypeListComponent implements OnInit {
  types = new MatTableDataSource<EquipmentTypeModel>();
  displayedColumns = ['name', 'description', 'options'];

  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private equipmentTypeService: EquipmentTypeService,
    private dialog: MatDialog,
    private notificationService: NotificationService,
  ) {}

  ngOnInit(): void {
    this.equipmentTypeService.getAllEquipmentTypes().subscribe({
      next: (data) => {
        this.types.data = data;
        this.types.sort = this.sort;
      },
      error: () => this.notificationService.showError('Wystąpił błąd'),
    });
  }

  addEquipmentType() {
    this.dialog.open(EquipmentTypeFormDialogComponent, {
      width: '600px',
    });
  }

  editEquipmentType(type: EquipmentTypeModel) {
    this.dialog.open(EquipmentTypeFormDialogComponent, {
      width: '600px',
      data: {
        type: type,
      },
    });
  }
}
