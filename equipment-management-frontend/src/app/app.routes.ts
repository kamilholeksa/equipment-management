import { Routes } from '@angular/router';
import { userRoutes } from './features/user/user.routes';
import { equipmentRoutes } from './features/equipment/equipment.routes';
import { equipmentTypeRoutes } from './features/equipment-type/equipment-type.routes';
import { addressRoutes } from './features/address/address.routes';
import { transferRoutes } from './features/transfer/transfer.routes';
import { serviceRequestRoutes } from './features/service-request/service-request.routes';
import { AccessDeniedComponent } from './shared/components/access-denied/access-denied.component';
import { LoginComponent } from './core/auth/components/login/login.component';

export const routes: Routes = [
  ...userRoutes,
  ...equipmentRoutes,
  ...equipmentTypeRoutes,
  ...addressRoutes,
  ...transferRoutes,
  ...serviceRequestRoutes,
  { path: 'login', component: LoginComponent },
  { path: 'access-denied', component: AccessDeniedComponent },
  { path: '', redirectTo: '/my-equipment', pathMatch: 'full' },
  { path: '**', redirectTo: '/my-equipment' },
];
