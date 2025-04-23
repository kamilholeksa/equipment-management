import {FormControl} from '@angular/forms';

export interface ServiceRequestFilter {
  id?: FormControl<number | null>;
  title?: FormControl<string | null>;
  status?: FormControl<string[] | null>;
  userId?: FormControl<number | null>;
  technicianId?: FormControl<number | null>;
}
