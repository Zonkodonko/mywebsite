import { TestBed } from '@angular/core/testing';

import { ArticleApi } from './article-api';

describe('ArticleApi', () => {
  let service: ArticleApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ArticleApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
