import { EquipmentModel } from '../equipment/equipment.model';
import { UserModel } from '../user/user.model';
import { ServiceRequestNoteModel } from '../service-request-note/service-request-note.model';
import { ServiceRequestStatusEnum } from '../../enums/service-request-status.enum';

export interface ServiceRequestModel {
  id: number;
  title: string;
  description: string;
  status: ServiceRequestStatusEnum;
  closeInfo: string;
  equipment: EquipmentModel;
  user: UserModel;
  technician: UserModel;
  createdBy: string;
  createdDate: Date;
  lastModifiedBy: string;
  lastModifiedDate: Date;
}

export interface ServiceRequestWithNotesModel extends ServiceRequestModel {
  notes: NoteModel[];
}

export type NoteModel = Omit<
  ServiceRequestNoteModel,
  'id' | 'serviceRequest' | 'lastModifiedBy' | 'lastModifiedDate'
>;
