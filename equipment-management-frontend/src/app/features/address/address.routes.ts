import { Routes } from '@angular/router';
import { authGuard } from '../../core/auth/guards/auth.guard';

export const addressRoutes: Routes = [
  {
    path: 'locations',
    loadComponent: () =>
      import('./components/address-list/address-list.component').then(
        (m) => m.AddressListComponent,
      ),
    data: { roles: ['ROLE_ADMIN'] },
    canActivate: [authGuard],
  },
];
