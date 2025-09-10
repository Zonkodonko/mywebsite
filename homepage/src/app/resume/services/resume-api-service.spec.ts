import { TestBed } from '@angular/core/testing';

import { ResumeApiService } from './resume-api-service';

describe('ResumeApiService', () => {
  let service: ResumeApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResumeApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
