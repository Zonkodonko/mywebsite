import {Component, OnInit} from '@angular/core';
import {de, en} from '../resources/resume';
import {ViewportScroller} from '@angular/common';
import {TranslateService} from '@ngx-translate/core';
import {ResumeData} from '../data/ResumeData';
import {ResumeApiService} from '../services/resume-api-service';

@Component({
  selector: 'app-resume',
  standalone: false,
  templateUrl: './resume-component.html',
  styleUrl: './resume-component.scss'
})
export class ResumeComponent implements OnInit {

  public resume!: ResumeData;

  public backgroundParticles: number[] = []

  constructor(public viewportScroller: ViewportScroller,
              private translate: TranslateService,
              private resumeService: ResumeApiService) {
    this.backgroundParticles = Array(50).fill(0);
  }

  ngOnInit(): void {
    this.translate.onLangChange.subscribe((l) => {
      this.fetchResume();
    })
    this.fetchResume();
  }

  private fetchResume() {
    this.resume = this.translate.getCurrentLang() === 'de' ? de : en;
    if(this.resume.skills.length === 0) {
      this.resumeService.fetchResume().subscribe(resume => {
        Object.assign(this.resume,resume);
      })
    }
  }

  get lang() {
    return this.translate.getCurrentLang();
  }

  /**
   * Scroll viewport to element with id
   * @param id of element
   */
  public scrollTo(id: string) {
    this.viewportScroller.scrollToAnchor(id, {behavior: 'smooth'})
  }

}
