import {NgModule} from '@angular/core';
import {CommonModule, DatePipe, NgOptimizedImage} from '@angular/common';
import {ResumeComponent} from './resume/resume-component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SharedModule} from '../shared/shared-module';
import {CareerComponent} from './career/career-component';
import {NgbCollapse} from '@ng-bootstrap/ng-bootstrap';
import { SkillOverviewComponent } from './skill-overview/skill-overview.component';
import {TranslatePipe} from '@ngx-translate/core';


@NgModule({
  declarations: [
    ResumeComponent,
    CareerComponent,
    SkillOverviewComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgOptimizedImage,
    SharedModule,
    NgbCollapse,
    TranslatePipe,
    FormsModule,
  ]
})
export class ResumeModule {
}
