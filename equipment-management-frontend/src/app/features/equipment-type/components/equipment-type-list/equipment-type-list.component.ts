import { Component, OnInit, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { EquipmentTypeService } from '../../services/equipment-type.service';
import { EquipmentTypeFormDialogComponent } from '../equipment-type-form-dialog/equipment-type-form-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import { EquipmentType } from '../../models/equipment-type.model';

@Component({
  selector: 'app-equipment-type-list',
  standalone: true,
  imports: [MatButtonModule, MatTableModule, MatSortModule],
  templateUrl: './equipment-type-list.component.html',
  styleUrl: './equipment-type-list.component.scss',
})
export class EquipmentTypeListComponent implements OnInit {
  types = new MatTableDataSource<EquipmentType>();
  displayedColumns = ['name', 'description', 'options'];

  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private equipmentTypeService: EquipmentTypeService,
    private dialog: MatDialog,
    private notificationService: NotificationService,
  ) {}

  ngOnInit(): void {
    this.loadData()
  }

  loadData(): void {
    this.equipmentTypeService.getAllEquipmentTypes().subscribe({
      next: (data) => {
        this.types.data = data;
        this.types.sort = this.sort;
      },
      error: () => this.notificationService.error(),
    });
  }

  addEquipmentType() {
    const dialogRef = this.dialog.open(EquipmentTypeFormDialogComponent, {
      width: '600px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    })
  }

  editEquipmentType(type: EquipmentType) {
    const dialogRef = this.dialog.open(EquipmentTypeFormDialogComponent, {
      width: '600px',
      data: {
        type: type,
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    })
  }

  deleteEquipmentType(typeId: number) {
    const confirmed = confirm('Are you sure you want to delete this type?');

    if (confirmed) {
      this.equipmentTypeService.deleteEquipmentType(typeId).subscribe({
        next: () => {
          this.loadData();
          this.notificationService.success('Type deleted successfully');
        },
        error: (err) => this.notificationService.error(err.error.message),
      });
    }
  }
}
