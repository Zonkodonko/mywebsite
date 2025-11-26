import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicOverview } from './topic-overview.component';

describe('BlogComponent', () => {
  let component: TopicOverview;
  let fixture: ComponentFixture<TopicOverview>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TopicOverview]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TopicOverview);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
