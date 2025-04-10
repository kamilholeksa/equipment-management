import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth/auth.service';
import {AsyncPipe, NgIf} from '@angular/common';
import { HasRoleDirective } from '../../../core/directives/has-role.directive';
import { HasAnyRoleDirective } from '../../../core/directives/has-any-role.directive';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, NgIf, HasRoleDirective, HasAnyRoleDirective, AsyncPipe],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent {
  constructor(protected authService: AuthService) {}

  // isLoggedInAndValidAccessToken(): boolean {
  //   return (
  //     this.authService.isLoggedIn() &&
  //     this.authService.account$.getValue() !== null
  //   );
  // }

  logout() {
    this.authService.logout();
  }
}
