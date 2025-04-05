import { TestBed } from '@angular/core/testing';

import { ServiceRequestNoteService } from './service-request-note.service';

describe('ServiceRequestNoteService', () => {
  let service: ServiceRequestNoteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceRequestNoteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
