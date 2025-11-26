import {ChangeDetectorRef, Component, ElementRef, Input, ViewChild} from '@angular/core';
import {ResumeApiService} from '../services/resume-api-service';
import {AuthenticationService} from '../../authentication/authentication.service';
import {TranslateService} from '@ngx-translate/core';
import {marked} from 'marked';

@Component({
  selector: 'app-aboutme',
  standalone: false,
  templateUrl: './aboutme.component.html',
  styleUrl: './aboutme.component.scss'
})
export class AboutmeComponent {

  public _aboutMe: Map<string,string> = new Map();
  public isEditMode: boolean = false;
  public showEditButton: boolean = false;
  private _originalText: string = "";

  @ViewChild('aboutMeInput') aboutMeInput!: ElementRef<HTMLTextAreaElement>;

  constructor(private resumeService: ResumeApiService,
              private authService: AuthenticationService,
              private changeDetector : ChangeDetectorRef,
              private langService: TranslateService,) {
  }

  @Input()
  public set aboutMe(aboutMe: {text: string, lang: string}) {
    this._aboutMe.set(aboutMe.lang, aboutMe.text);
    this._originalText = aboutMe.text;
  }

  get text(): string {
    return this._aboutMe.get(this.langService.getCurrentLang())!;
  }

  get formattedText(): string {
    return marked.parse(this._aboutMe.get(this.langService.getCurrentLang())!,{async: false});
  }

  set text(value: string) {
    this._aboutMe.set(this.langService.getCurrentLang(), value);
  }

  public get isLoggedIn() {
    return this.authService.isLoggedIn
  }

  public activateEditMode() {
    if(this.isLoggedIn) {
      this.isEditMode = true;
      this.changeDetector.detectChanges();//refresh input element ref
      this.aboutMeInput.nativeElement.focus();
    }
  }

  /**
   * Save about me - text to backend.
   */
  public saveAboutMe() {
    this.isEditMode = false;
    this.resumeService.updateAboutMe(this.text,this.langService.getCurrentLang()).subscribe({
      error: (error) => {
        this.text = this._originalText;
        this.showEditButton = false;
      },
    });
  }

  toggleEditButtonVisible(show: boolean) {
    if(this.isLoggedIn) {
      this.showEditButton = show;
    }
  }








}
