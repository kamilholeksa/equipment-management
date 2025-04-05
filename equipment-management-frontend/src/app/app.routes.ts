import { Routes } from '@angular/router';
import {authRoutes} from './components/auth/auth.routes';
import {userRoutes} from './components/user/user.routes';
import {equipmentRoutes} from './components/equipment/equipment.routes';
import {equipmentTypeRoutes} from './components/equipment-type/equipment-type.routes';
import {addressRoutes} from './components/address/address.routes';
import {transferRoutes} from './components/transfer/transfer.routes';
import {serviceRequestRoutes} from './components/service-request/service-request.routes';

export const routes: Routes = [
  ...authRoutes,
  ...userRoutes,
  ...equipmentRoutes,
  ...equipmentTypeRoutes,
  ...addressRoutes,
  ...transferRoutes,
  ...serviceRequestRoutes,
  { path: '', redirectTo: '/my-equipment', pathMatch: 'full' },
  { path: '**', redirectTo: '/my-equipment' },
];
