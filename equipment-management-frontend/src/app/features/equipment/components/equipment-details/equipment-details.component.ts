import { Component, OnInit } from '@angular/core';
import { EquipmentService } from '../../services/equipment.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe, Location, NgClass } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { EquipmentStatusDisplayPipe } from '../../../../shared/pipes/equipment-status-display.pipe';
import { MatDialog } from '@angular/material/dialog';
import { TransferFormDialogComponent } from '../../../transfer/components/transfer-form-dialog/transfer-form-dialog.component';
import { EquipmentHistoryDialogComponent } from '../../../equipment-history/components/equipment-history-dialog/equipment-history-dialog.component';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import { HasAnyRoleDirective } from '../../../../shared/directives/has-any-role.directive';
import { AuthService } from '../../../../core/auth/services/auth.service';
import { Equipment } from '../../models/equipment.model';

@Component({
  selector: 'app-equipment-details',
  standalone: true,
  imports: [
    DatePipe,
    MatButtonModule,
    EquipmentStatusDisplayPipe,
    HasAnyRoleDirective,
    NgClass,
  ],
  templateUrl: './equipment-details.component.html',
  styleUrl: './equipment-details.component.scss',
})
export class EquipmentDetailsComponent implements OnInit {
  equipment!: Equipment;

  constructor(
    private equipmentService: EquipmentService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private location: Location,
    private notificationService: NotificationService,
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe({
      next: ({ equipment }) => {
        this.equipment = equipment;
      },
      error: () => this.notificationService.error(),
    });
  }

  loadData() {
    this.equipmentService.getEquipment(this.equipment.id).subscribe({
      next: (equipment) => {
        this.equipment = equipment;
      },
      error: () => this.notificationService.error(),
    })
  }

  goBack() {
    this.location.back();
  }

  edit() {
    this.router.navigate(['equipment/edit', this.equipment.id]);
  }

  transfer() {
    this.dialog.open(TransferFormDialogComponent, {
      width: '600px',
      data: {
        equipmentId: this.equipment.id,
      },
    });
  }

  openHistoryDialog() {
    this.dialog.open(EquipmentHistoryDialogComponent, {
      width: '1200px',
      maxWidth: '100%',
      data: {
        equipmentId: this.equipment.id,
      },
    });
  }

  goToServiceRequests() {
    this.router.navigate(['service-requests/equipment', this.equipment.id]);
  }

  decommission() {
    const confirmed = confirm(
      'Are you sure you want withdraw this equipment from use?',
    );

    if (confirmed) {
      this.equipmentService.decommission(this.equipment.id).subscribe({
        next: () => {
          this.notificationService.success(
            'The equipment has been withdrawn from use',
          );
          this.loadData();
        },
        error: () => this.notificationService.error(),
      });
    }
  }

  canTransfer(): boolean {
    return (
      this.authService.hasAnyRole(['ROLE_ADMIN', 'ROLE_MANAGER']) ||
      this.equipment.user.id === this.authService.account()?.id
    );
  }

  getStatusClass(status: string): string {
    return this.equipmentService.getStatusClass(status);
  }
}
