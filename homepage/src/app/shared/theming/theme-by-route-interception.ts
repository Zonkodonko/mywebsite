import {Injectable, OnInit} from '@angular/core';
import {Theme, ThemeService} from './theme-service';
import {EventType, Router} from '@angular/router';

/**
 * Service that changes theme based on route.
 */
@Injectable({
  providedIn: 'root'
})
export class ThemeByRouteInterception implements OnInit {

  private readonly THEME_MAPPING = new Map<string, Theme>([
    ['blog', Theme.BLUE],
    ['resume', Theme.DEFAULT],
  ]);

  constructor(private themeService: ThemeService, private router: Router) {
    this.router.events.subscribe((e) => {
      if (e.type == EventType.NavigationEnd) {
        this.changeTheme(e.urlAfterRedirects);
      }
    });
  }

  ngOnInit(): void {
    const url = this.router.url;
    this.changeTheme(url);
  }

  private changeTheme(url: string): void {
    for (const [route, theme] of this.THEME_MAPPING) {
      if (url.includes(route)) {
        this.themeService.changeTheme(theme);
      }
    }
  }

}
