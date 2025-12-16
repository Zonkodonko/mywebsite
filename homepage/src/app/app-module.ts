import {inject, NgModule, provideEnvironmentInitializer} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing-module';
import {App} from './app';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {provideTranslateService, TranslateModule} from '@ngx-translate/core';
import {provideTranslateHttpLoader} from '@ngx-translate/http-loader';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {DatePipe, registerLocaleData} from '@angular/common';
import {UnauthorizedHandlerInterceptor} from './shared/http-interceptors/unauthorized-handler-interceptor';
import {FormsModule} from '@angular/forms';
import localeDe from '@angular/common/locales/de';
import localeDeExtra from '@angular/common/locales/extra/de';
import {ThemeByRouteInterception} from './shared/theming/theme-by-route-interception';

registerLocaleData(localeDe, 'de', localeDeExtra);

@NgModule({
  declarations: [
    App
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    TranslateModule.forRoot(),
    FormsModule
  ],
  providers: [
    DatePipe,
    provideHttpClient(withInterceptorsFromDi()),
    {provide: HTTP_INTERCEPTORS, useClass: UnauthorizedHandlerInterceptor, multi: true},
    provideTranslateService({
      lang: 'en',
      fallbackLang: 'en',
      loader: provideTranslateHttpLoader()
    }),
    provideEnvironmentInitializer(() => {
      inject(ThemeByRouteInterception);
    })
  ],
  bootstrap: [App]
})
export class AppModule {
  constructor() {
  }
}
