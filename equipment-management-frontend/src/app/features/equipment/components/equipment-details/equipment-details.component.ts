import { Component, OnInit } from '@angular/core';
import { EquipmentService } from '../../services/equipment.service';
import { ActivatedRoute, Router } from '@angular/router';
import {DatePipe, Location, NgClass, NgIf} from '@angular/common';
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
    NgIf,
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
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.equipmentService.getEquipment(id).subscribe({
      next: (data) => {
        this.equipment = data;
      },
      error: () => this.notificationService.error('Wystąpił błąd'),
    });
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
    const confirmed = confirm('Czy na pewno chcesz wycofać sprzęt z użytku?');

    if (confirmed) {
      this.equipmentService.decommission(this.equipment.id).subscribe({
        next: () => {
          this.notificationService.success('Sprzęt został wycofany z użytku');
          window.location.reload();
        },
        error: () => this.notificationService.error('Wystąpił błąd'),
      });
    }
  }

  canTransfer(): boolean {
    return (
      this.authService.hasAnyRole(['ROLE_ADMIN', 'ROLE_MANAGER']) ||
      this.equipment.user.username === this.authService.account()?.username
    );
  }

  getStatusClass(status: string): string {
    return this.equipmentService.getStatusClass(status);
  }
}
