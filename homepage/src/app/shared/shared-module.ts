import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IntersectAnimation } from './intersect-animation/intersect-animation';
import { AutoHeight } from './auto-resize/auto-height.directive';
import { LogoutNotifcationModal } from './logout-notifcation-modal/logout-notifcation-modal';



@NgModule({
  declarations: [
    IntersectAnimation,
    AutoHeight,
    LogoutNotifcationModal
  ],
  exports: [
    IntersectAnimation,
    AutoHeight
  ],
  imports: [
    CommonModule
  ]
})
export class SharedModule { }
