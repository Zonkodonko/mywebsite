import {AfterViewInit, Component, OnInit, signal} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {LoginComponent} from './authentication/login/login.component';
import {AuthenticationService} from './authentication/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: false,
  styleUrl: './app.scss'
})
export class App implements OnInit {
  protected readonly title = signal('homepage');

  constructor(private translateService: TranslateService, private modalService: NgbModal, private authService: AuthenticationService) {
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
    if(this.isLoggedIn) {
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
