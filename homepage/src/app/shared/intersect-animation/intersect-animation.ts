import {Directive, ElementRef, inject, OnDestroy, OnInit} from '@angular/core';



@Directive({
  selector: '[intersect-anim]',
  standalone: false
})
export class IntersectAnimation implements OnInit, OnDestroy{

  private el = inject(ElementRef)
  private intersectionObserver: any;


  constructor() {
  }

  ngOnInit(): void {
    setTimeout(() => {
      if(this.intersectionObserver == undefined) {
        this.intersectionObserver = this.getIntersectionObserver();
        this.intersectionObserver.observe(this.el.nativeElement);
        this.el.nativeElement.classList.add('intersection-observer');
      }
    }, 10)
  }

  private getIntersectionObserver() {
    return new IntersectionObserver(entries => {
      entries.forEach((entry) =>{
        if(entry.isIntersecting) {
          entry.target.classList.add('show');
        } else {
          entry.target.classList.remove('show');
        }
      })
    });
  }

  ngOnDestroy() {
    if (this.intersectionObserver) {
      this.intersectionObserver.disconnect();
      this.intersectionObserver = undefined;
    }
  }





}
