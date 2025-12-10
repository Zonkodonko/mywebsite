import { Pipe, PipeTransform } from '@angular/core';
import slugify from 'slugify';

@Pipe({
  name: 'slugify',
  standalone: false
})
export class SlugifyPipe implements PipeTransform {

  transform(value: string): unknown {
    return slugify(value)
  }

}
