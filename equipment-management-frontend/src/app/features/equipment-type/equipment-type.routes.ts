import { Routes } from '@angular/router';
import { EquipmentTypeListComponent } from './components/equipment-type-list/equipment-type-list.component';
import { authGuard } from '../../core/auth/guards/auth.guard';

export const equipmentTypeRoutes: Routes = [
  {
    path: 'equipment-types',
    component: EquipmentTypeListComponent,
    data: { roles: ['ROLE_ADMIN'] },
    canActivate: [authGuard],
  },
];
