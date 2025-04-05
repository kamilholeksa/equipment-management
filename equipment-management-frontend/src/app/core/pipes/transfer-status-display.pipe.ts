import { Pipe, PipeTransform } from '@angular/core';
import { TransferStatusEnum } from '../enums/transfer-status.enum';

@Pipe({
  name: 'transferStatusDisplay',
  standalone: true,
})
export class TransferStatusDisplayPipe implements PipeTransform {
  transform(value: TransferStatusEnum): string {
    const statusMap = {
      [TransferStatusEnum.PENDING]: 'Oczekujące',
      [TransferStatusEnum.ACCEPTED]: 'Zaakceptowane',
      [TransferStatusEnum.REJECTED]: 'Odrzcuone',
    };
    return statusMap[value] || value;
  }
}
