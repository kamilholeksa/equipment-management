import {
  ApplicationConfig,
  inject,
  provideAppInitializer,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {
  MAT_DATE_LOCALE,
  provideNativeDateAdapter,
} from '@angular/material/core';
import { PolishPaginatorIntl } from './core/config/material/polish-paginator-intl';
import { MatPaginatorIntl } from '@angular/material/paginator';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { AuthService } from './core/auth/services/auth.service';
import { authInterceptor } from './core/auth/interceptors/auth.interceptor';

export const fetchUserData = () => {
  const authService = inject(AuthService);
  return authService.fetchUserData();
};

export const appConfig: ApplicationConfig = {
  providers: [
    provideAppInitializer(fetchUserData),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient(withInterceptors([authInterceptor])),
    { provide: MAT_DATE_LOCALE, useValue: 'pl-PL' },
    { provide: MatPaginatorIntl, useClass: PolishPaginatorIntl },
    provideNativeDateAdapter(),
  ],
};
