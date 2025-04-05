import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceRequestNoteListComponent } from './service-request-note-list.component';

describe('ServiceRequestNoteListComponent', () => {
  let component: ServiceRequestNoteListComponent;
  let fixture: ComponentFixture<ServiceRequestNoteListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceRequestNoteListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServiceRequestNoteListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
