import { Component, OnInit, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { AddressModel } from '../../../core/models/address/address.model';
import { AddressService } from '../../../core/services/address.service';
import { AddressFormDialogComponent } from '../address-form-dialog/address-form-dialog.component';
import { NotificationService } from '../../../core/services/shared/notification.service';

@Component({
  selector: 'app-address-list',
  standalone: true,
  imports: [MatButtonModule, MatTableModule, MatSortModule],
  templateUrl: './address-list.component.html',
  styleUrl: './address-list.component.scss',
})
export class AddressListComponent implements OnInit {
  addresses = new MatTableDataSource<AddressModel>();
  displayedColumns = ['description', 'address', 'options'];

  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private addressService: AddressService,
    private dialog: MatDialog,
    private notificationService: NotificationService,
  ) {}

  ngOnInit(): void {
    this.addressService.getAllAddresses().subscribe({
      next: (data) => {
        this.addresses.data = data;
        this.addresses.sort = this.sort;
      },
      error: () => this.notificationService.showError('Wystąpił błąd'),
    });
  }

  addAddress() {
    this.dialog.open(AddressFormDialogComponent, {
      width: '600px',
    });
  }

  editAddress(address: AddressModel) {
    this.dialog.open(AddressFormDialogComponent, {
      width: '600px',
      data: {
        address: address,
      },
    });
  }
}
