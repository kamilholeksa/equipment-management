import { Routes } from '@angular/router';
import { EquipmentListComponent } from './components/equipment-list/equipment-list.component';
import { authGuard } from '../../core/auth/guards/auth.guard';
import { EquipmentDetailsComponent } from './components/equipment-details/equipment-details.component';
import { EquipmentFormComponent } from './components/equipment-form/equipment-form.component';

export const equipmentRoutes: Routes = [
  {
    path: 'my-equipment',
    canActivate: [authGuard],
    children: [
      {
        path: '',
        component: EquipmentListComponent,
        data: { showAllEquipment: false },
      },
      {
        path: ':id',
        component: EquipmentDetailsComponent,
      },
    ],
  },
  {
    path: 'equipment',
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER'] },
    children: [
      {
        path: '',
        component: EquipmentListComponent,
        data: { showAllEquipment: true },
      },
      {
        path: 'new',
        component: EquipmentFormComponent,
      },
      {
        path: 'edit/:id',
        component: EquipmentFormComponent,
      },
    ],
  },
  {
    path: 'equipment/:id',
    component: EquipmentDetailsComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_TECHNICIAN'] },
  },
];
