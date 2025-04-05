import { UserModel } from '../user/user.model';
import { AddressModel } from '../address/address.model';
import { EquipmentTypeModel } from '../equipment-type/equipment-type.model';
import { EquipmentStatusEnum } from '../../enums/equipment-status.enum';

export interface EquipmentModel {
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
  type: EquipmentTypeModel;
  address: AddressModel;
  user: UserModel;
  createdBy: string;
  createdDate: Date;
  lastModifiedBy: string;
  lastModifiedDate: Date;
}
