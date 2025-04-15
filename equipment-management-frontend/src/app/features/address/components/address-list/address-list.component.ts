import { Component, OnInit, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { AddressService } from '../../services/address.service';
import { AddressFormDialogComponent } from '../address-form-dialog/address-form-dialog.component';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import { Address } from '../../models/address.model';

@Component({
  selector: 'app-address-list',
  standalone: true,
  imports: [MatButtonModule, MatTableModule, MatSortModule],
  templateUrl: './address-list.component.html',
  styleUrl: './address-list.component.scss',
})
export class AddressListComponent implements OnInit {
  addresses = new MatTableDataSource<Address>();
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
      error: () => this.notificationService.error('Wystąpił błąd'),
    });
  }

  addAddress() {
    this.dialog.open(AddressFormDialogComponent, {
      width: '600px',
    });
  }

  editAddress(address: Address) {
    this.dialog.open(AddressFormDialogComponent, {
      width: '600px',
      data: {
        address: address,
      },
    });
  }
}
