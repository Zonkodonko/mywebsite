import { TestBed } from '@angular/core/testing';

import { ThemeByRouteInterception } from './theme-by-route-interception';

describe('ThemeByRouteInterception', () => {
  let service: ThemeByRouteInterception;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ThemeByRouteInterception);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
