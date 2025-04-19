import { Pipe, PipeTransform } from '@angular/core';
import { EquipmentStatusEnum } from '../enums/equipment-status.enum';

@Pipe({
  name: 'equipmentStatusDisplay',
  standalone: true,
})
export class EquipmentStatusDisplayPipe implements PipeTransform {
  transform(value: EquipmentStatusEnum): string {
    const statusMap = {
      [EquipmentStatusEnum.NEW]: 'New',
      [EquipmentStatusEnum.IN_PREPARATION]: 'In preparation',
      [EquipmentStatusEnum.IN_USE]: 'In use',
      [EquipmentStatusEnum.IN_REPAIR]: 'In repair',
      [EquipmentStatusEnum.RESERVE]: 'Reserve',
      [EquipmentStatusEnum.DECOMMISSIONED]: 'Decommissioned',
    };
    return statusMap[value] || value;
  }
}
