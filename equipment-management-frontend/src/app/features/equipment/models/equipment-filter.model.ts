import { FormControl } from '@angular/forms';

export interface EquipmentFilter {
  manufacturer?: FormControl<string | null>;
  model?: FormControl<string | null>;
  inventoryNumber?: FormControl<string | null>;
  serialNumber?: FormControl<string | null>;
  status?: FormControl<string[] | null>;
  userId?: FormControl<number[] | null>;
}
