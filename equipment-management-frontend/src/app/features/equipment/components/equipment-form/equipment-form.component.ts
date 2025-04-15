import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { EquipmentService } from '../../services/equipment.service';
import { EquipmentTypeService } from '../../../equipment-type/services/equipment-type.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Location, NgForOf, NgIf } from '@angular/common';
import { EquipmentStatusEnum } from '../../../../shared/enums/equipment-status.enum';
import { EquipmentStatusDisplayPipe } from '../../../../shared/pipes/equipment-status-display.pipe';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import { AddressService } from '../../../address/services/address.service';
import dayjs from 'dayjs';
import { EquipmentType } from '../../../equipment-type/models/equipment-type.model';
import { Address } from '../../../address/models/address.model';
import { Equipment } from '../../models/equipment.model';

@Component({
  selector: 'app-equipment-form',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatSelectModule,
    MatDatepickerModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    NgForOf,
    NgIf,
    EquipmentStatusDisplayPipe,
  ],
  templateUrl: './equipment-form.component.html',
  styleUrls: ['./equipment-form.component.scss'],
})
export class EquipmentFormComponent implements OnInit {
  equipmentForm: FormGroup;
  equipmentTypeList!: EquipmentType[];
  addressList!: Address[];
  statuses = Object.values(EquipmentStatusEnum);
  equipmentId!: number;
  isEditMode = false;

  constructor(
    private equipmentService: EquipmentService,
    private equipmentTypeService: EquipmentTypeService,
    private addressService: AddressService,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location,
    private notificationService: NotificationService,
  ) {
    this.equipmentForm = new FormGroup({
      manufacturer: new FormControl('', Validators.required),
      model: new FormControl('', Validators.required),
      description: new FormControl(''),
      inventoryNumber: new FormControl('', Validators.required),
      serialNumber: new FormControl(''),
      status: new FormControl(EquipmentStatusEnum.NEW),
      location: new FormControl(''),
      purchaseDate: new FormControl(null),
      warrantyUntil: new FormControl(null),
      typeId: new FormControl(null),
      addressId: new FormControl(null),
      userId: new FormControl(null),
    });
  }

  ngOnInit(): void {
    this.equipmentTypeService.getAllEquipmentTypes().subscribe({
      next: (data) => (this.equipmentTypeList = data),
      error: () => this.notificationService.error('Wystąpił błąd'),
    });

    this.addressService.getAllAddresses().subscribe({
      next: (data) => (this.addressList = data),
      error: () => this.notificationService.error('Wystąpił błąd'),
    });

    this.equipmentId = +this.route.snapshot.paramMap.get('id')!;
    if (this.equipmentId) {
      this.isEditMode = true;
      this.equipmentService.getEquipment(this.equipmentId).subscribe({
        next: (equipment) => this.populateForm(equipment),
        error: () => this.notificationService.error('Wystąpił błąd'),
      });
    }
  }

  populateForm(equipment: Equipment): void {
    this.equipmentForm.patchValue({
      manufacturer: equipment.manufacturer,
      model: equipment.model,
      description: equipment.description,
      inventoryNumber: equipment.inventoryNumber,
      serialNumber: equipment.serialNumber,
      status: equipment.status,
      location: equipment.location,
      purchaseDate: equipment.purchaseDate,
      warrantyUntil: equipment.warrantyUntil,
      typeId: equipment.type?.id,
      addressId: equipment.address?.id,
      userId: equipment.user?.id,
    });
  }

  onSubmit(): void {
    if (this.equipmentForm.valid) {
      const equipmentData = this.equipmentForm.value;
      equipmentData.status = this.isEditMode
        ? equipmentData.status
        : EquipmentStatusEnum.NEW;

      equipmentData.purchaseDate = equipmentData.purchaseDate
        ? dayjs(equipmentData.purchaseDate).format('YYYY-MM-DD')
        : null;

      equipmentData.warrantyUntil = equipmentData.warrantyUntil
        ? dayjs(equipmentData.warrantyUntil).format('YYYY-MM-DD')
        : null;

      if (this.equipmentId) {
        this.equipmentService
          .updateEquipment(this.equipmentId, equipmentData)
          .subscribe({
            next: () => {
              this.goBack();
            },
            error: (err) =>
              this.notificationService.error(
                err.error.message ? err.error.message : 'Wystąpił błąd',
              ),
          });
      } else {
        this.equipmentService.createEquipment(equipmentData).subscribe({
          next: () => {
            this.goBack();
          },
          error: (err) =>
            this.notificationService.error(
              err.error.message ? err.error.message : 'Wystąpił błąd',
            ),
        });
      }
    }
  }

  goBack() {
    this.location.back();
  }
}
