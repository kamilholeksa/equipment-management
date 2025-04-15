import { UserModel } from '../../user/models/user.model';
import { TransferStatusEnum } from '../../../shared/enums/transfer-status.enum';
import { Equipment } from '../../equipment/models/equipment.model';

export interface Transfer {
  id: number;
  requestDate: Date;
  decisionDate: Date;
  status: TransferStatusEnum;
  equipment: Equipment;
  transferor: UserModel;
  obtainer: UserModel;
  createdBy: string;
  createdDate: Date;
  lastModifiedBy: string;
  lastModifiedDate: Date;
}
