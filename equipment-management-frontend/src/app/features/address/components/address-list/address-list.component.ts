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
  displayedColumns = ['description', 'postalCode', 'city', 'street', 'number', 'options'];

  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private addressService: AddressService,
    private dialog: MatDialog,
    private notificationService: NotificationService,
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.addressService.getAllAddresses().subscribe({
      next: (data) => {
        this.addresses.data = data;
        this.addresses.sort = this.sort;
      },
      error: () => this.notificationService.error(),
    });
  }

  addAddress() {
    const dialogRef = this.dialog.open(AddressFormDialogComponent, {
      width: '600px',
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadData();
      }
    });
  }

  editAddress(address: Address) {
    const dialogRef = this.dialog.open(AddressFormDialogComponent, {
      width: '600px',
      data: {
        address: address,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadData();
      }
    });
  }

  deleteAddress(addressId: number) {
    const confirmed = confirm('Are you sure you want to delete this address?');

    if (confirmed) {
      this.addressService.deleteAddress(addressId).subscribe({
        next: () => {
          this.loadData();
          this.notificationService.success("Location deleted successfully");
        },
        error: (err) => this.notificationService.error(err.error.message),
      });
    }
  }
}
