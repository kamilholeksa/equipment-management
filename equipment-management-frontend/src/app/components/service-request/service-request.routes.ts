import { Routes } from '@angular/router';
import { AuthGuard } from '../../core/guards/auth.guard';
import { ServiceRequestListComponent } from './service-request-list/service-request-list.component';
import { ServiceRequestDetailsComponent } from './service-request-details/service-request-details.component';

export const serviceRequestRoutes: Routes = [
  {
    path: 'service-requests',
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_TECHNICIAN'] },
    children: [
      {
        path: '',
        component: ServiceRequestListComponent,
      },
      {
        path: ':id',
        component: ServiceRequestDetailsComponent,
      },
    ],
  },
  {
    path: 'service-requests/equipment/:equipmentId',
    component: ServiceRequestListComponent,
    canActivate: [AuthGuard],
  },
];
