import { Routes } from '@angular/router';
import { EquipmentListComponent } from './equipment-list/equipment-list.component';
import { AuthGuard } from '../../core/guards/auth.guard';
import { EquipmentDetailsComponent } from './equipment-details/equipment-details.component';
import { EquipmentFormComponent } from './equipment-form/equipment-form.component';

export const equipmentRoutes: Routes = [
  {
    path: 'my-equipment',
    canActivate: [AuthGuard],
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
    canActivate: [AuthGuard],
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
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_TECHNICIAN'] },
  },
];
