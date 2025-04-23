import {FormControl} from '@angular/forms';

export interface UserFilter {
  id?: FormControl<number | null>;
  firstName?: FormControl<string | null>;
  lastName?: FormControl<string | null>;
  username?: FormControl<string | null>;
  email?: FormControl<string | null>;
  phoneNumber?: FormControl<string | null>;
  roles?: FormControl<string[] | null>;
  active?: FormControl<boolean | null>;
}
