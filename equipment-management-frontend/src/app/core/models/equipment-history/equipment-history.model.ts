import { EquipmentModel } from '../equipment/equipment.model';
import { UserModel } from '../user/user.model';
import { EquipmentStatusEnum } from '../../enums/equipment-status.enum';

export interface EquipmentHistoryModel {
  id: number;
  equipment: EquipmentModel;
  oldStatus: EquipmentStatusEnum;
  newStatus: EquipmentStatusEnum;
  oldLocation: string;
  newLocation: string;
  oldUser: UserModel;
  newUser: UserModel;
  changeDate: Date;
}
