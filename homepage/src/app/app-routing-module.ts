import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ResumeComponent} from './resume/resume/resume-component';
import {ArticleOverview} from './blog/blog/article-overview/article-overview/article-overview';

const routes: Routes = [
  {
    path: 'resume',
    component: ResumeComponent
  },
  {
    path: '',
    redirectTo: 'resume',
    pathMatch: 'full'
  },
  {
    path: 'blog/:topicId',
    pathMatch: 'full',
    component: ArticleOverview
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    bindToComponentInputs: true
  })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
