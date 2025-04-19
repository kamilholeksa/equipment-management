import { User } from '../../user/models/user.model';
import { EquipmentStatusEnum } from '../../../shared/enums/equipment-status.enum';
import { Equipment } from '../../equipment/models/equipment.model';

export interface EquipmentHistory {
  id: number;
  equipment: Equipment;
  oldStatus: EquipmentStatusEnum;
  newStatus: EquipmentStatusEnum;
  oldLocation: string;
  newLocation: string;
  oldUser: User;
  newUser: User;
  changeDate: Date;
}
