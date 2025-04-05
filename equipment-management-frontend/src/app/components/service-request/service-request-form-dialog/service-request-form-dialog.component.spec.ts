import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceRequestFormDialogComponent } from './service-request-form-dialog.component';

describe('ServiceRequestFormComponent', () => {
  let component: ServiceRequestFormDialogComponent;
  let fixture: ComponentFixture<ServiceRequestFormDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceRequestFormDialogComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ServiceRequestFormDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
