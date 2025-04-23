import { Pipe, PipeTransform } from '@angular/core';
import { ServiceRequestStatusEnum } from '../enums/service-request-status.enum';

@Pipe({
  name: 'serviceRequestStatusDisplay',
  standalone: true,
})
export class ServiceRequestStatusDisplayPipe implements PipeTransform {
  transform(value: ServiceRequestStatusEnum): string {
    const statusMap = {
      [ServiceRequestStatusEnum.NEW]: 'New',
      [ServiceRequestStatusEnum.ACCEPTED]: 'Accepted',
      [ServiceRequestStatusEnum.IN_PROGRESS]: 'In progress',
      [ServiceRequestStatusEnum.CANCELLED]: 'Cancelled',
      [ServiceRequestStatusEnum.CLOSED]: 'Closed',
    };
    return statusMap[value] || value;
  }
}
