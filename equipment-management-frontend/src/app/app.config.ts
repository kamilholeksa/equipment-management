import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {MAT_DATE_LOCALE, provideNativeDateAdapter} from '@angular/material/core';
import {PolishPaginatorIntl} from './core/config/material/polish-paginator-intl';
import {MatPaginatorIntl} from '@angular/material/paginator';
import {AuthInterceptor} from './core/interceptors/auth.interceptor';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient(withInterceptorsFromDi()),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
    { provide: MAT_DATE_LOCALE, useValue: 'pl-PL' },
    { provide: MatPaginatorIntl, useClass: PolishPaginatorIntl },
    provideNativeDateAdapter(),
  ]
};
