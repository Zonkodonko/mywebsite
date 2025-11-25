import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ResumeComponent} from './resume/resume/resume-component';
import {BlogComponent} from './blog/blog/blog-component';

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
    path: 'projects',
    pathMatch: 'full',
    component: BlogComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
