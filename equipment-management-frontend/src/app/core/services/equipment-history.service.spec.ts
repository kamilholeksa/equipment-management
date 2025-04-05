import { TestBed } from '@angular/core/testing';

import { EquipmentHistoryService } from './equipment-history.service';

describe('EquipmentHistoryService', () => {
  let service: EquipmentHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EquipmentHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
