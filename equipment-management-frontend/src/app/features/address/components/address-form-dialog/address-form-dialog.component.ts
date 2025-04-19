import { Component, Inject, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import { AddressService } from '../../services/address.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { NgIf } from '@angular/common';
import { Address } from '../../models/address.model';

@Component({
  selector: 'app-address-form-dialog',
  standalone: true,
  imports: [
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './address-form-dialog.component.html',
  styleUrl: './address-form-dialog.component.scss',
})
export class AddressFormDialogComponent implements OnInit {
  addressForm: FormGroup;
  isEditMode = false;

  constructor(
    private dialogRef: MatDialogRef<AddressFormDialogComponent>,
    private addressService: AddressService,
    private notificationService: NotificationService,
    @Inject(MAT_DIALOG_DATA) public data: { address: Address },
  ) {
    this.addressForm = new FormGroup({
      description: new FormControl(''),
      postalCode: new FormControl('', Validators.required),
      city: new FormControl('', Validators.required),
      street: new FormControl('', Validators.required),
      number: new FormControl('', Validators.required),
    });
  }

  ngOnInit(): void {
    if (this.data?.address) {
      this.isEditMode = true;
      this.populateForm(this.data.address);
    }
  }

  onSubmit() {
    if (this.addressForm.valid) {
      const data = this.addressForm.value;

      if (this.isEditMode) {
        this.addressService
          .updateAddress(this.data.address.id, data)
          .subscribe({
            next: () => {
              this.notificationService.success(
                'Location has been updated',
              );
              this.dialogRef.close(true);
            },
            error: (err) =>
              this.notificationService.error(
                err.error.message,
              ),
          });
      } else {
        this.addressService.createAddress(data).subscribe({
          next: () => {
            this.notificationService.success('Location has been created');
            this.dialogRef.close(true);
          },
          error: () =>
            this.notificationService.error(),
        });
      }
    }
  }

  private populateForm(address: Address) {
    this.addressForm.patchValue({
      description: address.description,
      postalCode: address.postalCode,
      city: address.city,
      street: address.street,
      number: address.number,
    });
  }
}
