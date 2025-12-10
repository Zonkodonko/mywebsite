import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MultiLanguageTextInput } from './multi-language-text-input';

describe('MultiLanguageTextInput', () => {
  let component: MultiLanguageTextInput;
  let fixture: ComponentFixture<MultiLanguageTextInput>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MultiLanguageTextInput]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MultiLanguageTextInput);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
