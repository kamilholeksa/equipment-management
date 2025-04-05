import { Routes } from '@angular/router';
import { AuthGuard } from '../../core/guards/auth.guard';
import { TransferListComponent } from './transfer-list/transfer-list.component';

export const transferRoutes: Routes = [
  {
    path: 'transfers',
    component: TransferListComponent,
    canActivate: [AuthGuard],
    data: {
      showAll: true,
      showPending: false,
      roles: ['ROLE_ADMIN', 'ROLE_MANAGER'],
    },
  },
  {
    path: 'my-transfers',
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        component: TransferListComponent,
        data: { showAll: false, showPending: true },
      },
      {
        path: 'all',
        component: TransferListComponent,
        data: { showAll: false, showPending: false },
      },
    ],
  },
];
