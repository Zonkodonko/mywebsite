import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing-module';
import {App} from './app';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {provideTranslateService, TranslateModule} from '@ngx-translate/core';
import {provideTranslateHttpLoader} from '@ngx-translate/http-loader';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {DatePipe} from '@angular/common';
import {UnauthorizedHandlerInterceptor} from './shared/http-interceptors/unauthorized-handler-interceptor';


@NgModule({
  declarations: [
    App
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    TranslateModule.forRoot()
  ],
  providers: [
    DatePipe,
    provideHttpClient(withInterceptorsFromDi()),
    {provide: HTTP_INTERCEPTORS,useClass: UnauthorizedHandlerInterceptor, multi: true},
    provideTranslateService({
      lang: 'en',
      fallbackLang: 'en',
      loader: provideTranslateHttpLoader()
    })
  ],
  bootstrap: [App]
})
export class AppModule {
  constructor() {
  }
}
