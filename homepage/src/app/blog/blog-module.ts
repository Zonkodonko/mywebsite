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
import {ArticleCard} from './blog/article-overview/article-card/article-card';
import {RouterLink} from '@angular/router';
import { ArticleDialog } from './article-dialog/article-dialog';
import { ArticleOverview } from './blog/article-overview/article-overview/article-overview';
import { ArticleComponent } from './blog/article-component/article-component';

@NgModule({
  declarations: [
    TopicOverview,
    TopicDialog,
    TopicCard,
    ArticleCard,
    ArticleDialog,
    ArticleOverview,
    ArticleComponent
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
    RouterLink,
  ],
  exports: [
    TopicOverview
  ],
})
export class BlogModule {
}
