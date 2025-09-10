import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ResumeComponent} from './resume/resume/resume-component';

const routes: Routes = [
  {
    path: 'resume',
    component: ResumeComponent
  },
  {
    path: '',
    redirectTo: 'resume',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
