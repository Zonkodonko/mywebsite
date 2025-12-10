import { TestBed } from '@angular/core/testing';

import { TopicApi } from './topic-api';

describe('TopicApi', () => {
  let service: TopicApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TopicApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
