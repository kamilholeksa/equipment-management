import { ServiceRequestModel } from '../service-request/service-request.model';

export interface ServiceRequestNoteModel {
  id: number;
  description: string;
  serviceRequest: ServiceRequestModel;
  createdBy: string;
  createdDate: Date;
  lastModifiedBy: string;
  lastModifiedDate: Date;
}
