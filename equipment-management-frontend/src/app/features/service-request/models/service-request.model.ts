import { User } from '../../user/models/user.model';
import { ServiceRequestNote } from './service-request-note.model';
import { ServiceRequestStatusEnum } from '../../../shared/enums/service-request-status.enum';
import { Equipment } from '../../equipment/models/equipment.model';

export interface ServiceRequest {
  id: number;
  title: string;
  description: string;
  status: ServiceRequestStatusEnum;
  closeInfo: string;
  equipment: Equipment;
  user: User;
  technician: User;
  createdBy: string;
  createdDate: Date;
  lastModifiedBy: string;
  lastModifiedDate: Date;
}

export interface ServiceRequestWithNotesModel extends ServiceRequest {
  notes: NoteModel[];
}

export type NoteModel = Omit<
  ServiceRequestNote,
  'id' | 'serviceRequest' | 'lastModifiedBy' | 'lastModifiedDate'
>;
