import { Pipe, PipeTransform } from '@angular/core';
import { RoleDisplayPipe } from './role-display.pipe';

@Pipe({
  name: 'roleListDisplay',
  standalone: true,
})
export class RoleListDisplayPipe implements PipeTransform {
  transform(roles: string[]): string {
    return roles.map((role) => new RoleDisplayPipe().transform(role)).join(', ');
  }
}
