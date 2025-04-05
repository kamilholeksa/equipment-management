import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceRequestNoteDialogComponent } from './service-request-note-dialog.component';

describe('ServiceRequestNoteDialogComponent', () => {
  let component: ServiceRequestNoteDialogComponent;
  let fixture: ComponentFixture<ServiceRequestNoteDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceRequestNoteDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServiceRequestNoteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
