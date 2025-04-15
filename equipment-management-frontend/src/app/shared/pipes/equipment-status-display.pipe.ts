import { Pipe, PipeTransform } from '@angular/core';
import { EquipmentStatusEnum } from '../enums/equipment-status.enum';

@Pipe({
  name: 'equipmentStatusDisplay',
  standalone: true,
})
export class EquipmentStatusDisplayPipe implements PipeTransform {
  transform(value: EquipmentStatusEnum): string {
    const statusMap = {
      [EquipmentStatusEnum.NEW]: 'Nowy',
      [EquipmentStatusEnum.IN_PREPARATION]: 'W przygotowaniu',
      [EquipmentStatusEnum.IN_USE]: 'W użyciu',
      [EquipmentStatusEnum.IN_REPAIR]: 'W naprawie',
      [EquipmentStatusEnum.RESERVE]: 'Rezerwa',
      [EquipmentStatusEnum.DECOMMISSIONED]: 'Zlikwidowany',
    };
    return statusMap[value] || value;
  }
}
