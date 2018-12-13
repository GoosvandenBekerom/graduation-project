import {Directive, ElementRef, HostListener} from '@angular/core';
import {Router} from '@angular/router';

@Directive({
  // tslint:disable-next-line:directive-selector
  selector: 'a[externalUrl]'
})
export class ExternalUrlDirective {
  constructor(private el: ElementRef, private router: Router) {}

  @HostListener('click', ['$event'])
  clicked(event: Event) {
    const url = this.el.nativeElement.href;
    if (!url) {
      return;
    }

      this.router.navigate(['/redirectExternal', { url: url }], {
        skipLocationChange: true
      });
      event.preventDefault();
  }
}
