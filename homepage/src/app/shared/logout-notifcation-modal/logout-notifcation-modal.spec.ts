import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogoutNotifcationModal } from './logout-notifcation-modal';

describe('LogoutNotifcationModal', () => {
  let component: LogoutNotifcationModal;
  let fixture: ComponentFixture<LogoutNotifcationModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LogoutNotifcationModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LogoutNotifcationModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
