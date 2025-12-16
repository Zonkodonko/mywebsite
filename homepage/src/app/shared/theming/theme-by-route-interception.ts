import {Injectable} from '@angular/core';
import {Theme, ThemeService} from './theme-service';
import {EventType, Router} from '@angular/router';

/**
 * Service that changes theme based on route.
 */
@Injectable({
  providedIn: 'root'
})
export class ThemeByRouteInterception {

  private readonly THEME_MAPPING = new Map<string, Theme>([
    ['blog', Theme.BLUE],
    ['resume', Theme.DEFAULT],
  ]);

  constructor(private themeService: ThemeService, private router: Router) {
    this.router.events.subscribe((e) => {
      if (e.type == EventType.NavigationEnd) {
        for (const [route, theme] of this.THEME_MAPPING) {
          if (e.url.includes(route)) {
            this.themeService.changeTheme(theme);
          }
        }
      }
    });
  }

}
