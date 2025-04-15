import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const requiredRoles = route.data['roles'] as string[];

  // If not logged in then redirect to login page
  if (!authService.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  // If no roles are defined in route then access route
  if (!requiredRoles || requiredRoles.length === 0) {
    return true;
  }
  // If roles are defined in route and user has any of these roles then access route
  if (authService.hasAnyRole(requiredRoles)) {
    return true;
  }
  // In other cases return to access-denied page
  router.navigate(['/access-denied']);
  return false;
};
