import {EventEmitter, Injectable} from '@angular/core';
import {DomSanitizer} from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {


  /**
   * Emits when the theme changes.
   */
  public themeChange: EventEmitter<Theme> = new EventEmitter<Theme>();

  currentTheme: Theme = Theme.DEFAULT;


  private readonly THEME_KEY = 'theme';

  constructor(private sanitizer: DomSanitizer) {
    localStorage.getItem(this.THEME_KEY) === Theme.BLUE ? this.changeTheme(Theme.BLUE) : this.changeTheme(Theme.DEFAULT);
  }

  /**
   * Get resource url for current css.
   */
  public getThemeUrl() {
    return this.sanitizer.bypassSecurityTrustResourceUrl(`${this.currentTheme}.css`);
  }

  /**
   * Change theme.
   * @param theme to change to.
   */
  public changeTheme(theme: Theme) {
    this.currentTheme = theme;
    this.themeChange.emit(theme);
    localStorage.setItem(this.THEME_KEY, theme);
  }


}

/**
 * Available themes.
 */
export enum Theme {
  DEFAULT = 'default',
  BLUE = 'blue'
}
