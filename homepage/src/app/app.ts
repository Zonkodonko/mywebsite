import {Component, OnInit, signal} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {LoginComponent} from './authentication/login/login.component';
import {AuthenticationService} from './authentication/authentication.service';
import {Theme, ThemeService} from './shared/theming/theme-service';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: false,
  styleUrl: './app.scss'
})
export class App implements OnInit {
  protected readonly title = signal('homepage');


  public isPopUpOpen: boolean = false;
  private optionsHoverTimer?: NodeJS.Timeout;

  public showMoreOptions: boolean = false;

  constructor(private translateService: TranslateService,
              private modalService: NgbModal,
              private authService: AuthenticationService,
              private themeService: ThemeService,
  ) {
  }

  public isShowLoginButton: boolean = false;

  ngOnInit(): void {
    if (navigator.language.includes('de')) {
      this.translateService.use('de')
    } else {
      this.translateService.use('en');
    }
  }

  /**
   * Start options hover timer.
   */
  onHoverInOptions() {
    this.optionsHoverTimer = setTimeout(() => {
      this.showMoreOptions = true;
    }, 2000);
  }

  /**
   * Cancel options hover timer
   */
  onHoverOutOptions() {
    if (this.optionsHoverTimer && !this.isPopUpOpen) {
      clearTimeout(this.optionsHoverTimer);
      this.showMoreOptions = false;
    }
  }

  /**
   * Get styles url for current theme.
   */
  getStyles() {
    return this.themeService.getThemeUrl();
  }

  get currentTheme() {
    return this.themeService.currentTheme;
  }

  set currentTheme(value: Theme) {
    this.themeService.changeTheme(value)
  }

  get allThemes(): Theme[] {
    return Object.values(Theme);
  }

  /**
   * Switch between English and German
   */
  public toggleLanguage() {
    this.translateService.use(this.currentLanguage === 'en' ? 'de' : 'en');
  }

  get currentLanguage() {
    return this.translateService.getCurrentLang() ?? this.translateService.getFallbackLang();
  }

  get isLoggedIn() {
    return this.authService.isLoggedIn;
  }

  clickLoginLogoutButton() {
    if (this.isLoggedIn) {
      this.authService.logout();
    } else {
      this.modalService.open(LoginComponent, {
        centered: true,
        size: 'sm'
      });
    }
  }

  showLoginButton() {
    this.isShowLoginButton = true;
  }

  hideLoginButton() {
    this.isShowLoginButton = false;
  }
}
