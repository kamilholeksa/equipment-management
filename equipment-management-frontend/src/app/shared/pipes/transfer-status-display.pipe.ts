import { Pipe, PipeTransform } from '@angular/core';
import { TransferStatusEnum } from '../enums/transfer-status.enum';

@Pipe({
  name: 'transferStatusDisplay',
  standalone: true,
})
export class TransferStatusDisplayPipe implements PipeTransform {
  transform(value: TransferStatusEnum): string {
    const statusMap = {
      [TransferStatusEnum.PENDING]: 'Pending',
      [TransferStatusEnum.ACCEPTED]: 'Accepted',
      [TransferStatusEnum.REJECTED]: 'Rejected',
    };
    return statusMap[value] || value;
  }
}
