import { ServiceRequest } from './service-request.model';

export interface ServiceRequestNote {
  id: number;
  description: string;
  serviceRequest: ServiceRequest;
  createdBy: string;
  createdDate: Date;
  lastModifiedBy: string;
  lastModifiedDate: Date;
}
