import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransferAcceptDialogComponent } from './transfer-accept-dialog.component';

describe('TransferAcceptDialogComponent', () => {
  let component: TransferAcceptDialogComponent;
  let fixture: ComponentFixture<TransferAcceptDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransferAcceptDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransferAcceptDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
