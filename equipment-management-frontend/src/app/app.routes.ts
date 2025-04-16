import { Routes } from '@angular/router';
import { userRoutes } from './features/user/user.routes';
import { equipmentRoutes } from './features/equipment/equipment.routes';
import { equipmentTypeRoutes } from './features/equipment-type/equipment-type.routes';
import { addressRoutes } from './features/address/address.routes';
import { transferRoutes } from './features/transfer/transfer.routes';
import { serviceRequestRoutes } from './features/service-request/service-request.routes';

export const routes: Routes = [
  ...userRoutes,
  ...equipmentRoutes,
  ...equipmentTypeRoutes,
  ...addressRoutes,
  ...transferRoutes,
  ...serviceRequestRoutes,
  {
    path: 'login',
    loadComponent: () =>
      import('./core/auth/components/login/login.component').then(
        (m) => m.LoginComponent,
      ),
  },
  {
    path: 'access-denied',
    loadComponent: () =>
      import('./shared/components/access-denied/access-denied.component').then(
        (m) => m.AccessDeniedComponent,
      ),
  },
  { path: '', redirectTo: '/my-equipment', pathMatch: 'full' },
  { path: '**', redirectTo: '/my-equipment' },
];
