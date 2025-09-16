import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IntersectAnimation } from './intersect-animation/intersect-animation';
import { AutoHeight } from './auto-resize/auto-height.directive';



@NgModule({
  declarations: [
    IntersectAnimation,
    AutoHeight
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
