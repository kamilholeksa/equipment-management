import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'roleDisplay',
  standalone: true,
})
export class RoleDisplayPipe implements PipeTransform {
  transform(value: string): string {
    const statusMap = {
      'ROLE_USER': 'User',
      'ROLE_ADMIN': 'Administrator',
      'ROLE_MANAGER': 'Manager',
      'ROLE_TECHNICIAN': 'Technician',
    };

    return statusMap[value as keyof typeof statusMap] || value;
  }
}
