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
import { NotificationService } from '../../../core/services/shared/notification.service';
import { AddressModel } from '../../../core/models/address/address.model';
import { AddressService } from '../../../core/services/address.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-address-form-dialog',
  standalone: true,
  imports: [
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    NgIf,
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
    @Inject(MAT_DIALOG_DATA) public data: { address: AddressModel },
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
              this.notificationService.showSuccess(
                'Lokalizacja została zaktualizowana',
              );
              this.dialogRef.close();
              window.location.reload();
            },
            error: (err) =>
              this.notificationService.showError(
                err.error.message ? err.error.message : 'Wystąpił błąd',
              ),
          });
      } else {
        this.addressService.createAddress(data).subscribe({
          next: () => {
            this.notificationService.showSuccess(
              'Lokalizacja została utworzona',
            );
            this.dialogRef.close();
            window.location.reload();
          },
          error: () =>
            this.notificationService.showError('Wystąpił błąd przy zapisie'),
        });
      }
    }
  }

  private populateForm(address: AddressModel) {
    this.addressForm.patchValue({
      description: address.description,
      postalCode: address.postalCode,
      city: address.city,
      street: address.street,
      number: address.number,
    });
  }
}
