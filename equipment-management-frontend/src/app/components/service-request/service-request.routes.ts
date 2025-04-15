import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth.guard';
import { ServiceRequestListComponent } from './service-request-list/service-request-list.component';
import { ServiceRequestDetailsComponent } from './service-request-details/service-request-details.component';

export const serviceRequestRoutes: Routes = [
  {
    path: 'service-requests',
    canActivate: [authGuard],
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
    canActivate: [authGuard],
  },
];
