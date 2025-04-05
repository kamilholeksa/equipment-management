import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { NgIf } from '@angular/common';
import { HasRoleDirective } from '../../../core/directives/has-role.directive';
import { HasAnyRoleDirective } from '../../../core/directives/has-any-role.directive';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, NgIf, HasRoleDirective, HasAnyRoleDirective],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent {
  constructor(
    private router: Router,
    protected authService: AuthService,
  ) {}

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
