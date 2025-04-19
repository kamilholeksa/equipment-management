import {
  ActivatedRouteSnapshot,
  MaybeAsync,
  RedirectCommand,
  Resolve,
  RouterStateSnapshot,
} from '@angular/router';
import { Injectable } from '@angular/core';
import { ServiceRequestService } from '../services/service-request.service';
import { ServiceRequest } from '../models/service-request.model';

@Injectable({
  providedIn: 'root',
})
export class ServiceRequestResolver implements Resolve<ServiceRequest> {
  constructor(private service: ServiceRequestService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
  ): MaybeAsync<ServiceRequest | RedirectCommand> {
    return this.service.getServiceRequest(Number(route.paramMap.get('id')));
  }
}
