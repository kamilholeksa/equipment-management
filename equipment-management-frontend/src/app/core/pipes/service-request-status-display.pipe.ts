import { Pipe, PipeTransform } from '@angular/core';
import { ServiceRequestStatusEnum } from '../enums/service-request-status.enum';

@Pipe({
  name: 'serviceRequestStatusDisplay',
  standalone: true,
})
export class TransferStatusDisplayPipe implements PipeTransform {
  transform(value: ServiceRequestStatusEnum): string {
    const statusMap = {
      [ServiceRequestStatusEnum.NEW]: 'Nowe',
      [ServiceRequestStatusEnum.ACCEPTED]: 'Zaakceptowane',
      [ServiceRequestStatusEnum.IN_PROGRESS]: 'W trakcie realizacji',
      [ServiceRequestStatusEnum.CANCELLED]: 'Anulowane',
      [ServiceRequestStatusEnum.CLOSED]: 'Zakończone',
    };
    return statusMap[value] || value;
  }
}
