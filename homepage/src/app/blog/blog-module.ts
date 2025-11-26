import {NgModule} from '@angular/core';

import {CommonModule, NgOptimizedImage} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SharedModule} from '../shared/shared-module';
import {NgbCollapse} from '@ng-bootstrap/ng-bootstrap';
import {TranslatePipe} from '@ngx-translate/core';
import {AppModule} from '../app-module';
import {TopicOverview} from './blog/topic-overview/topic-overview.component';
import {TopicDialog} from './topic-dialog/topic-dialog';
import {TopicCard} from './blog/topic-card/topic-card';

@NgModule({
  declarations: [
    TopicOverview,
    TopicDialog,
    TopicCard
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgOptimizedImage,
    SharedModule,
    NgbCollapse,
    TranslatePipe,
    FormsModule,
    AppModule,
  ],
  exports: [
    TopicOverview
  ],
})
export class BlogModule {
}
