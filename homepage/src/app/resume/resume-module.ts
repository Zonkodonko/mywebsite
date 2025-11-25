import {NgModule} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {ResumeComponent} from './resume/resume-component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SharedModule} from '../shared/shared-module';
import {CareerComponent} from './career/career-component';
import {NgbCollapse} from '@ng-bootstrap/ng-bootstrap';
import {SkillOverviewComponent} from './skill-overview/skill-overview.component';
import {TranslatePipe} from '@ngx-translate/core';
import {AboutmeComponent} from './aboutme/aboutme.component';
import {AppModule} from "../app-module";
import {BlogModule} from '../blog/blog-module';


@NgModule({
  declarations: [
    ResumeComponent,
    CareerComponent,
    SkillOverviewComponent,
    AboutmeComponent
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
    BlogModule
  ]
})
export class ResumeModule {
}
