import { User } from '../../user/models/user.model';
import { EquipmentStatusEnum } from '../../../shared/enums/equipment-status.enum';
import { EquipmentType } from '../../equipment-type/models/equipment-type.model';
import { Address } from '../../address/models/address.model';

export interface Equipment {
  id: number;
  manufacturer: string;
  model: string;
  description: string;
  inventoryNumber: string;
  serialNumber: string;
  status: EquipmentStatusEnum;
  location: string;
  purchaseDate: Date;
  warrantyUntil: Date;
  withdrawalDate: Date;
  type: EquipmentType;
  address: Address;
  user: User;
  createdBy: string;
  createdDate: Date;
  lastModifiedBy: string;
  lastModifiedDate: Date;
}
