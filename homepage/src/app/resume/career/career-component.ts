import {Component, Inject, Input, LOCALE_ID} from '@angular/core';
import {DatePipe} from '@angular/common';
import {Career} from '../data/ResumeData';

@Component({
  selector: 'app-career-component',
  standalone: false,
  templateUrl: './career-component.html',
  styleUrl: './career-component.scss'
})
export class CareerComponent {

  @Input()
  public career!: Career;

  constructor(private datePipe: DatePipe,
              @Inject(LOCALE_ID) public locale: string) {
  }

  dateFormat(timestamp: number) {
    return this.datePipe.transform(timestamp, 'MMM y', '+0100', this.locale);
  }

}
