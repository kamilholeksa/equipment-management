import { Routes } from '@angular/router';
import { UserListComponent } from './components/user-list/user-list.component';
import { authGuard } from '../../core/auth/guards/auth.guard';
import { UserResolver } from './resolvers/user.resolver';
import {UserProfileResolver} from './resolvers/user-profile.resolver';

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
        resolve: {
          user: UserResolver,
        },
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
    resolve: {
      account: UserProfileResolver,
    },
  },
];
