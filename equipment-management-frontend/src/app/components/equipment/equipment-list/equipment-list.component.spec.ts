import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EquipmentListComponent } from './equipment-list.component';

describe('EquipmentComponent', () => {
  let component: EquipmentListComponent;
  let fixture: ComponentFixture<EquipmentListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EquipmentListComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(EquipmentListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
