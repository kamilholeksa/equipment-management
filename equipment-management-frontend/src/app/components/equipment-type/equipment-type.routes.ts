import { Routes } from '@angular/router';
import { EquipmentTypeListComponent } from './equipment-type-list/equipment-type-list.component';
import { authGuard } from '../../core/guards/auth.guard';

export const equipmentTypeRoutes: Routes = [
  {
    path: 'equipment-types',
    component: EquipmentTypeListComponent,
    data: { roles: ['ROLE_ADMIN'] },
    canActivate: [authGuard],
  },
];
