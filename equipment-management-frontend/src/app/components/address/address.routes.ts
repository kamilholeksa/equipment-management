import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth.guard';
import { AddressListComponent } from './address-list/address-list.component';

export const addressRoutes: Routes = [
  {
    path: 'locations',
    component: AddressListComponent,
    data: { roles: ['ROLE_ADMIN'] },
    canActivate: [authGuard],
  },
];
