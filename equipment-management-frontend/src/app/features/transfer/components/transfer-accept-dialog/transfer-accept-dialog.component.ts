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
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { TransferService } from '../../services/transfer.service';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import { AddressService } from '../../../address/services/address.service';
import { AcceptTransferRequest } from '../../models/accept-transfer-request.model';
import { MatInputModule } from '@angular/material/input';
import { Address } from '../../../address/models/address.model';

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
  ],
  templateUrl: './transfer-accept-dialog.component.html',
  styleUrl: './transfer-accept-dialog.component.scss',
})
export class TransferAcceptDialogComponent implements OnInit {
  form: FormGroup;
  addressList!: Address[];

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
      error: () => this.notificationService.error(),
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
            this.notificationService.success(result.message);
            this.dialogRef.close(true);
          },
          error: () => this.notificationService.error(),
        });
    }
  }
}
