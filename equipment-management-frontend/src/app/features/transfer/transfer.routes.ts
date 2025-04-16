import { Routes } from '@angular/router';
import { authGuard } from '../../core/auth/guards/auth.guard';

export const transferRoutes: Routes = [
  {
    path: 'transfers',
    loadComponent: () =>
      import('./components/transfer-list/transfer-list.component').then(
        (m) => m.TransferListComponent,
      ),
    canActivate: [authGuard],
    data: {
      showAll: true,
      showPending: false,
      roles: ['ROLE_ADMIN', 'ROLE_MANAGER'],
    },
  },
  {
    path: 'my-transfers',
    canActivate: [authGuard],
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./components/transfer-list/transfer-list.component').then(
            (m) => m.TransferListComponent,
          ),
        data: { showAll: false, showPending: true },
      },
      {
        path: 'all',
        loadComponent: () =>
          import('./components/transfer-list/transfer-list.component').then(
            (m) => m.TransferListComponent,
          ),
        data: { showAll: false, showPending: false },
      },
    ],
  },
];
