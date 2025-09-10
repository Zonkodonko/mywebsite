import {AfterViewInit, Component, OnInit, signal} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {LoginComponent} from './authentication/login/login.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: false,
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('homepage');

  constructor(private translateService: TranslateService, private modalService: NgbModal) {
  }

  public isShowLoginButton: boolean = false;

  /**
   * Switch between English and German
   */
  public toggleLanguage() {
    console.log("lang before: " + this.currentLanguage);
    this.translateService.use(this.currentLanguage === 'en' ? 'de' : 'en');
    console.log(this.currentLanguage);
  }

  get currentLanguage() {
    return this.translateService.getCurrentLang() ?? this.translateService.getFallbackLang();
  }

  openLoginDialog() {
    this.modalService.open(LoginComponent, {
      centered: true,
      size: 'sm'
    });
  }

  showLoginButton() {
    console.log("show login button");
    this.isShowLoginButton = true;
  }

  hideLoginButton() {
    this.isShowLoginButton = false;
  }
}
