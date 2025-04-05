import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceRequestDetailsDialogComponent } from './service-request-details-dialog.component';

describe('ServiceRequestDetailsDialogComponent', () => {
  let component: ServiceRequestDetailsDialogComponent;
  let fixture: ComponentFixture<ServiceRequestDetailsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceRequestDetailsDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServiceRequestDetailsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
