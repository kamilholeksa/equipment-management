import { Routes } from '@angular/router';
import { authGuard } from '../../core/auth/guards/auth.guard';
import { ServiceRequestResolver } from './resolvers/service-request.resolver';

export const serviceRequestRoutes: Routes = [
  {
    path: 'service-requests',
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_TECHNICIAN'] },
    children: [
      {
        path: '',
        loadComponent: () =>
          import(
            './components/service-request-list/service-request-list.component'
          ).then((m) => m.ServiceRequestListComponent),
      },
      {
        path: ':id',
        loadComponent: () =>
          import(
            './components/service-request-details/service-request-details.component'
          ).then((m) => m.ServiceRequestDetailsComponent),
        resolve: {
          serviceRequest: ServiceRequestResolver,
        },
      },
    ],
  },
  {
    path: 'service-requests/equipment/:equipmentId',
    loadComponent: () =>
      import(
        './components/service-request-list/service-request-list.component'
      ).then((m) => m.ServiceRequestListComponent),
    canActivate: [authGuard],
  },
];
