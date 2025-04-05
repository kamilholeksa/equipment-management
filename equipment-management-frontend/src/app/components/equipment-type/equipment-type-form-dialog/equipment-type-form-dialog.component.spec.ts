import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EquipmentTypeFormDialogComponent } from './equipment-type-form-dialog.component';

describe('EquipmentTypeFormDialogComponent', () => {
  let component: EquipmentTypeFormDialogComponent;
  let fixture: ComponentFixture<EquipmentTypeFormDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EquipmentTypeFormDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EquipmentTypeFormDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
