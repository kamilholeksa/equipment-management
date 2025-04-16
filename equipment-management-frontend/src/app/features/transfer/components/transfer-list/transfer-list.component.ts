import { Component, OnInit, ViewChild } from '@angular/core';
import { TransferService } from '../../services/transfer.service';
import { Transfer } from '../../models/transfer.model';
import { MatSort, MatSortModule } from '@angular/material/sort';
import {
  MatPaginator,
  MatPaginatorModule,
  PageEvent,
} from '@angular/material/paginator';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import {NgClass, NgIf} from '@angular/common';
import { TransferStatusDisplayPipe } from '../../../../shared/pipes/transfer-status-display.pipe';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import { MatDialog } from '@angular/material/dialog';
import { TransferAcceptDialogComponent } from '../transfer-accept-dialog/transfer-accept-dialog.component';
import { AuthService } from '../../../../core/auth/services/auth.service';

@Component({
  selector: 'app-transfer-list',
  standalone: true,
  imports: [
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatButtonModule,
    NgIf,
    TransferStatusDisplayPipe,
    RouterLink,
    NgClass,
  ],
  templateUrl: './transfer-list.component.html',
  styleUrl: './transfer-list.component.scss',
})
export class TransferListComponent implements OnInit {
  showAll = false;
  showPending = true;
  transfers: Transfer[] = [];
  length = 0;
  pageSize = 10;
  pageIndex = 0;
  sortField = 'id';
  sortOrder = 'desc';
  displayedColumns: string[] = [];

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private transferService: TransferService,
    private router: Router,
    private route: ActivatedRoute,
    private notificationService: NotificationService,
    private dialog: MatDialog,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.showAll = this.route.snapshot.data['showAll'];
    this.showPending = this.route.snapshot.data['showPending'];
    this.setDisplayedColumns();
    this.loadData();
  }

  loadData() {
    let observable;

    if (this.showAll) {
      if (this.authService.hasAnyRole(['ROLE_ADMIN', 'ROLE_MANAGER'])) {
        observable = this.transferService.getAllTransfers(
          this.pageIndex,
          this.pageSize,
          this.sortField,
          this.sortOrder,
        );
      } else {
        observable = this.transferService.getMyTransfers(
          this.pageIndex,
          this.pageSize,
          this.sortField,
          this.sortOrder,
        );
      }
    } else {
      if (this.showPending) {
        observable = this.transferService.getTransfersToAccept(
          this.pageIndex,
          this.pageSize,
          this.sortField,
          this.sortOrder,
        );
      } else {
        observable = this.transferService.getMyTransfers(
          this.pageIndex,
          this.pageSize,
          this.sortField,
          this.sortOrder,
        );
      }
    }

    observable.subscribe({
      next: (data) => {
        this.transfers = data.content;
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

  private setDisplayedColumns() {
    if (this.showAll || !this.showPending) {
      this.displayedColumns = [
        'requestDate',
        'decisionDate',
        'equipment',
        'transferor',
        'obtainer',
        'status',
      ];
    } else {
      this.displayedColumns = [
        'requestDate',
        'equipment',
        'transferor',
        'options',
      ];
    }
  }

  accept(id: number, equipmentId: number) {
    this.dialog.open(TransferAcceptDialogComponent, {
      width: '600px',
      data: {
        transferId: id,
        equipmentId: equipmentId,
      },
    });
  }

  reject(id: number) {
    const confirmed = confirm('Czy na pewno chcesz odrzucić przekazanie?');

    if (confirmed) {
      this.transferService.rejectTransfer(id).subscribe({
        next: (result) => {
          this.notificationService.success(result.message);
          window.location.reload();
        },
        error: () => this.notificationService.error('Wystąpił błąd'),
      });
    }
  }

  getStatusClass(status: string): string {
    return this.transferService.getStatusClass(status);
  }
}
