import {
  ActivatedRouteSnapshot,
  MaybeAsync,
  RedirectCommand,
  Resolve,
  RouterStateSnapshot,
} from '@angular/router';
import { Equipment } from '../models/equipment.model';
import { EquipmentService } from '../services/equipment.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class EquipmentResolver implements Resolve<Equipment> {
  constructor(private service: EquipmentService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
  ): MaybeAsync<Equipment | RedirectCommand> {
    return this.service.getEquipment(Number(route.paramMap.get('id')));
  }
}
