import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IntersectAnimation } from './intersect-animation/intersect-animation';



@NgModule({
  declarations: [
    IntersectAnimation
  ],
  exports: [
    IntersectAnimation
  ],
  imports: [
    CommonModule
  ]
})
export class SharedModule { }
