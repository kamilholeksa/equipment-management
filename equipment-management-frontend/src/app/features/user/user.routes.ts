import { Routes } from '@angular/router';
import { UserListComponent } from './components/user-list/user-list.component';
import { authGuard } from '../../core/auth/guards/auth.guard';

export const userRoutes: Routes = [
  {
    path: 'users',
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN'] },
    children: [
      {
        path: '',
        component: UserListComponent,
      },
      {
        path: 'new',
        loadComponent: () =>
          import('./components/user-form/user-form.component').then(
            (m) => m.UserFormComponent,
          ),
      },
      {
        path: ':id',
        loadComponent: () =>
          import('./components/user-details/user-details.component').then(
            (m) => m.UserDetailsComponent,
          ),
      },
      {
        path: 'edit/:id',
        loadComponent: () =>
          import('./components/user-form/user-form.component').then(
            (m) => m.UserFormComponent,
          ),
      },
    ],
  },
  {
    path: 'profile',
    loadComponent: () =>
      import('./components/user-profile/user-profile.component').then(
        (m) => m.UserProfileComponent,
      ),
    canActivate: [authGuard],
  },
];
