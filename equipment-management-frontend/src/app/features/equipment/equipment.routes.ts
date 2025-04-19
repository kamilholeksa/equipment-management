import { Routes } from '@angular/router';
import { authGuard } from '../../core/auth/guards/auth.guard';
import { EquipmentResolver } from './resolvers/equipment.resolver';

export const equipmentRoutes: Routes = [
  {
    path: 'my-equipment',
    canActivate: [authGuard],
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./components/equipment-list/equipment-list.component').then(
            (m) => m.EquipmentListComponent,
          ),
        data: { showAllEquipment: false },
      },
      {
        path: ':id',
        loadComponent: () =>
          import(
            './components/equipment-details/equipment-details.component'
          ).then((m) => m.EquipmentDetailsComponent),
        resolve: {
          equipment: EquipmentResolver,
        },
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
        loadComponent: () =>
          import('./components/equipment-list/equipment-list.component').then(
            (m) => m.EquipmentListComponent,
          ),
        data: { showAllEquipment: true },
      },
      {
        path: 'new',
        loadComponent: () =>
          import('./components/equipment-form/equipment-form.component').then(
            (m) => m.EquipmentFormComponent,
          ),
      },
      {
        path: 'edit/:id',
        loadComponent: () =>
          import('./components/equipment-form/equipment-form.component').then(
            (m) => m.EquipmentFormComponent,
          ),
      },
    ],
  },
  {
    path: 'equipment/:id',
    loadComponent: () =>
      import('./components/equipment-details/equipment-details.component').then(
        (m) => m.EquipmentDetailsComponent,
      ),
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_TECHNICIAN'] },
    resolve: {
      equipment: EquipmentResolver,
    },
  },
];
