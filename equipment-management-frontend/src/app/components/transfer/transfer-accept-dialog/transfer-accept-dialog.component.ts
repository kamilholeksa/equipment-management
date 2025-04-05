import { Component, Inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatOption } from '@angular/material/core';
import { MatSelect } from '@angular/material/select';
import { NgForOf, NgIf } from '@angular/common';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { TransferService } from '../../../core/services/transfer.service';
import { NotificationService } from '../../../core/services/shared/notification.service';
import { AddressModel } from '../../../core/models/address/address.model';
import { AddressService } from '../../../core/services/address.service';
import { AcceptTransferRequest } from '../../../core/models/transfer/accept-transfer-request.model';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-transfer-accept-dialog',
  standalone: true,
  imports: [
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatOption,
    MatSelect,
    NgForOf,
    NgIf,
  ],
  templateUrl: './transfer-accept-dialog.component.html',
  styleUrl: './transfer-accept-dialog.component.scss',
})
export class TransferAcceptDialogComponent implements OnInit {
  form: FormGroup;
  addressList!: AddressModel[];

  constructor(
    private dialogRef: MatDialogRef<TransferAcceptDialogComponent>,
    private transferService: TransferService,
    private addressService: AddressService,
    private notificationService: NotificationService,
    @Inject(MAT_DIALOG_DATA)
    public data: {
      transferId: number;
      equipmentId: number;
    },
  ) {
    this.form = new FormGroup({
      newLocation: new FormControl('', Validators.required),
      newAddressId: new FormControl(null, Validators.required),
    });
  }

  ngOnInit(): void {
    this.addressService.getAllAddresses().subscribe({
      next: (data) => (this.addressList = data),
      error: () => this.notificationService.showError('Wystąpił błąd'),
    });
  }

  onSubmit() {
    if (this.form.valid) {
      const request: AcceptTransferRequest = this.form.value;
      request.equipmentId = this.data.equipmentId;

      this.transferService
        .acceptTransfer(this.data.transferId, request)
        .subscribe({
          next: (result) => {
            this.notificationService.showSuccess(result.message);
            this.dialogRef.close();
            window.location.reload();
          },
          error: () => this.notificationService.showError('Wystąpił błąd'),
        });
    }
  }
}
