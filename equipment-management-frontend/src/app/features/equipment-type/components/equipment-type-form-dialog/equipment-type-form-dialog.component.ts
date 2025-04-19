import { Component, Inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { NotificationService } from '../../../../core/notification/services/notification.service';
import { EquipmentTypeService } from '../../services/equipment-type.service';
import { EquipmentType } from '../../models/equipment-type.model';

@Component({
  selector: 'app-equipment-type-form-dialog',
  standalone: true,
  imports: [
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './equipment-type-form-dialog.component.html',
  styleUrl: './equipment-type-form-dialog.component.scss',
})
export class EquipmentTypeFormDialogComponent implements OnInit {
  typeForm: FormGroup;
  isEditMode = false;

  constructor(
    private dialogRef: MatDialogRef<EquipmentTypeFormDialogComponent>,
    private equipmentTypeService: EquipmentTypeService,
    private notificationService: NotificationService,
    @Inject(MAT_DIALOG_DATA) public data: { type: EquipmentType },
  ) {
    this.typeForm = new FormGroup({
      name: new FormControl('', Validators.required),
      description: new FormControl(''),
    });
  }

  ngOnInit(): void {
    if (this.data?.type) {
      this.isEditMode = true;
      this.populateForm(this.data.type);
    }
  }

  onSubmit() {
    if (this.typeForm.valid) {
      const data = this.typeForm.value;
      const observable = this.isEditMode
        ? this.equipmentTypeService.updateEquipmentType(this.data.type.id, data)
        : this.equipmentTypeService.createEquipmentType(data);

      observable.subscribe({
        next: () => {
          this.dialogRef.close(true);
        },
        error: () => this.notificationService.error(),
      });
    }
  }

  private populateForm(type: EquipmentType) {
    this.typeForm.patchValue({
      name: type.name,
      description: type.description,
    });
  }
}
