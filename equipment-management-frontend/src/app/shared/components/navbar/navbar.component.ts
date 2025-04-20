import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/auth/services/auth.service';
import { HasRoleDirective } from '../../directives/has-role.directive';
import { HasAnyRoleDirective } from '../../directives/has-any-role.directive';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, HasRoleDirective, HasAnyRoleDirective],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent {
  constructor(protected authService: AuthService) {}

  logout() {
    this.authService.logout();
  }
}
