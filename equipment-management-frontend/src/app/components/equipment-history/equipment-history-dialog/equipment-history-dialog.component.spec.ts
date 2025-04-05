import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EquipmentHistoryDialogComponent } from './equipment-history-dialog.component';

describe('EquipmentHistoryModalComponent', () => {
  let component: EquipmentHistoryDialogComponent;
  let fixture: ComponentFixture<EquipmentHistoryDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EquipmentHistoryDialogComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(EquipmentHistoryDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
