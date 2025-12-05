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


  constructor(private sanitizer: DomSanitizer) {
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
  }


}

/**
 * Available themes.
 */
export enum Theme {
  DEFAULT = 'default',
  BLUE = 'blue'
}
