import { Component, OnInit, ViewChild } from '@angular/core';
import { TransferService } from '../../services/transfer.service';
import { Transfer } from '../../models/transfer.model';
import { MatSort, MatSortModule } from '@angular/material/sort';
import {
  MatPaginator,
  MatPaginatorModule,
  PageEvent,
} from '@angular/material/paginator';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { NgClass } from '@angular/common';
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
    TransferStatusDisplayPipe,
    RouterLink,
    NgClass,
  ],
  templateUrl: './transfer-list.component.html',
  styleUrl: './transfer-list.component.scss',
})
export class TransferListComponent implements OnInit {
  mode: string = 'myAll';
  transfers: Transfer[] = [];
  length = 0;
  pageSize = 10;
  pageIndex = 0;
  sort = 'id,desc';
  displayedColumns: string[] = [];

  @ViewChild(MatSort) matSort!: MatSort;
  @ViewChild(MatPaginator) matPaginator!: MatPaginator;

  constructor(
    private transferService: TransferService,
    private route: ActivatedRoute,
    private notificationService: NotificationService,
    private dialog: MatDialog,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.mode = this.route.snapshot.data['mode'];
    this.setDisplayedColumns();
    this.loadData();
  }

  loadData() {
    let observable;

    switch (this.mode) {
      case 'all':
        observable = this.transferService.getAllTransfers(
            this.pageIndex,
            this.pageSize,
            this.sort,
          );
        break;

      case 'myAll':
        observable = this.transferService.getMyTransfers(
          this.pageIndex,
          this.pageSize,
          this.sort,
        );
        break;

      case 'myPending':
      default:
        observable = this.transferService.getTransfersToAccept(
          this.pageIndex,
          this.pageSize,
          this.sort,
        );
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
    const sortField = this.matSort.active;
    const sortOrder = this.matSort.direction;

    if (sortField && sortOrder) {
      this.sort = `${sortField},${sortOrder}`;
    } else {
      this.sort = 'id,desc';
    }
    this.loadData();
  }

  private setDisplayedColumns() {
    switch (this.mode) {
      case 'all':
      case 'myAll':
        this.displayedColumns = [
          'requestDate',
          'decisionDate',
          'equipment',
          'transferor',
          'obtainer',
          'status',
        ];
        break;

      case 'myPending':
        this.displayedColumns = [
          'requestDate',
          'equipment',
          'transferor',
          'options',
        ];
        break;
    }
  }

  accept(id: number, equipmentId: number) {
    const dialogRef = this.dialog.open(TransferAcceptDialogComponent, {
      width: '600px',
      data: {
        transferId: id,
        equipmentId: equipmentId,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadData();
      }
    });
  }

  reject(id: number) {
    const confirmed = confirm('Are you sure you want to reject this transfer?');

    if (confirmed) {
      this.transferService.rejectTransfer(id).subscribe({
        next: (result) => {
          this.notificationService.success(result.message);
          this.loadData();
        },
        error: () => this.notificationService.error(),
      });
    }
  }

  getStatusClass(status: string): string {
    return this.transferService.getStatusClass(status);
  }
}
