import { Routes } from '@angular/router';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { UserListComponent } from './user-list/user-list.component';
import { AuthGuard } from '../../core/guards/auth.guard';
import { UserFormComponent } from './user-form/user-form.component';
import { UserDetailsComponent } from './user-details/user-details.component';

export const userRoutes: Routes = [
  {
    path: 'users',
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_ADMIN'] },
    children: [
      {
        path: '',
        component: UserListComponent,
      },
      {
        path: 'new',
        component: UserFormComponent,
      },
      {
        path: ':id',
        component: UserDetailsComponent,
      },
      {
        path: 'edit/:id',
        component: UserFormComponent,
      },
    ],
  },
  {
    path: 'profile',
    component: UserProfileComponent,
    canActivate: [AuthGuard],
  },
];
