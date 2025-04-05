import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignmentChangeDialogComponent } from './assignment-change-dialog.component';

describe('AssignmentChangeDialogComponent', () => {
  let component: AssignmentChangeDialogComponent;
  let fixture: ComponentFixture<AssignmentChangeDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssignmentChangeDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssignmentChangeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
