import {AfterViewInit, Directive, ElementRef, HostListener} from '@angular/core';

@Directive({
  selector: '[autoheight]',
  standalone: false
})
export class AutoHeight implements AfterViewInit{

  private timer: any;

  constructor(private el: ElementRef<HTMLTextAreaElement>) {}

  ngAfterViewInit() {
    setTimeout(() => this.resize(),1000);
  }

  @HostListener('input')
  @HostListener('change')
  onInput() {
    this.resize();
  }

  /**
   * Resize on window resize. With debouncing
   */
  @HostListener('window:resize')
  onResize() {
    if(this.timer != null) {
      clearTimeout(this.timer);
    }
    this.timer = setTimeout(() => {
      this.resize()
    },100)
  }

  private resize() {
    const element = this.el.nativeElement;
    const cs = getComputedStyle(element);
    element.style.overflowY = 'hidden';
    element.style.height = 'auto';
    element.style.width = '100%';

    setTimeout(() => {
      const border = element.offsetHeight - element.clientHeight;
      const isBorderBox = cs.boxSizing === 'border-box';

      // Zielhöhe bestimmen
      let target = element.scrollHeight;
      if (isBorderBox) {
        target += border;
      }
      // Setzen
      element.style.height = `${target}px`;
      element.style.removeProperty("width");
    },100)
  }


}
