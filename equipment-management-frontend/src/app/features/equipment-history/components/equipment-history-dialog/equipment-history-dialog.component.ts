import { Component, Inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { DatePipe, NgForOf } from '@angular/common';
import { EquipmentHistoryService } from '../../services/equipment-history.service';
import { EquipmentStatusDisplayPipe } from '../../../../shared/pipes/equipment-status-display.pipe';
import { MatCardModule } from '@angular/material/card';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import { EquipmentHistory } from '../../models/equipment-history.model';

@Component({
  selector: 'app-equipment-history-modal',
  standalone: true,
  imports: [
    MatDialogModule,
    MatButtonModule,
    DatePipe,
    EquipmentStatusDisplayPipe,
    MatCardModule,
    NgForOf,
  ],
  templateUrl: './equipment-history-dialog.component.html',
  styleUrl: './equipment-history-dialog.component.scss',
})
export class EquipmentHistoryDialogComponent implements OnInit {
  history!: EquipmentHistory[];

  constructor(
    private equipmentHistoryService: EquipmentHistoryService,
    @Inject(MAT_DIALOG_DATA) public data: { equipmentId: number },
    private notificationService: NotificationService,
  ) {}

  ngOnInit(): void {
    this.equipmentHistoryService
      .getHistoryByEquipmentId(this.data.equipmentId)
      .subscribe({
        next: (data) => {
          data.sort(
            (a, b) =>
              new Date(b.changeDate).getTime() -
              new Date(a.changeDate).getTime(),
          );
          this.history = data;
        },
        error: () => this.notificationService.error(),
      });
  }
}
