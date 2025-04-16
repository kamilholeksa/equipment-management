import { Routes } from '@angular/router';
import { authGuard } from '../../core/auth/guards/auth.guard';

export const equipmentTypeRoutes: Routes = [
  {
    path: 'equipment-types',
    loadComponent: () =>
      import(
        './components/equipment-type-list/equipment-type-list.component'
      ).then((m) => m.EquipmentTypeListComponent),
    data: { roles: ['ROLE_ADMIN'] },
    canActivate: [authGuard],
  },
];
