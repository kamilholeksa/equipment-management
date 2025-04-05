import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceRequestCloseDialogComponent } from './service-request-close-dialog.component';

describe('ServiceRequestCloseDialogComponent', () => {
  let component: ServiceRequestCloseDialogComponent;
  let fixture: ComponentFixture<ServiceRequestCloseDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceRequestCloseDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServiceRequestCloseDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
