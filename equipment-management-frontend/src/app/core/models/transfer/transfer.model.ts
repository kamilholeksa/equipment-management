import { EquipmentModel } from '../equipment/equipment.model';
import { UserModel } from '../user/user.model';
import { TransferStatusEnum } from '../../enums/transfer-status.enum';

export interface TransferModel {
  id: number;
  requestDate: Date;
  decisionDate: Date;
  status: TransferStatusEnum;
  equipment: EquipmentModel;
  transferor: UserModel;
  obtainer: UserModel;
  createdBy: string;
  createdDate: Date;
  lastModifiedBy: string;
  lastModifiedDate: Date;
}
