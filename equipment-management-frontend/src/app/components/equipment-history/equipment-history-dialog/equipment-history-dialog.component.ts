import { Component, Inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { DatePipe, NgForOf, NgIf } from '@angular/common';
import { EquipmentHistoryModel } from '../../../core/models/equipment-history/equipment-history.model';
import { EquipmentHistoryService } from '../../../core/services/equipment-history.service';
import { EquipmentStatusDisplayPipe } from '../../../core/pipes/equipment-status-display.pipe';
import { MatCardModule } from '@angular/material/card';
import { NotificationService } from '../../../core/services/shared/notification.service';

@Component({
  selector: 'app-equipment-history-modal',
  standalone: true,
  imports: [
    MatDialogModule,
    MatButtonModule,
    NgIf,
    DatePipe,
    EquipmentStatusDisplayPipe,
    MatCardModule,
    NgForOf,
  ],
  templateUrl: './equipment-history-dialog.component.html',
  styleUrl: './equipment-history-dialog.component.scss',
})
export class EquipmentHistoryDialogComponent implements OnInit {
  history!: EquipmentHistoryModel[];

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
        error: () => this.notificationService.showError('Wystąpił błąd'),
      });
  }
}
