import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ArticleWithoutContent} from '../../../data/BlogTypes';
import {TranslateService} from '@ngx-translate/core';
import environment from '../../../../../environment';
import {isStringEmpty} from '../../../../shared/utils/utils';
import {localeIdMapping} from '../../../../shared/translation/Translation';
import {AuthenticationService} from '../../../../authentication/authentication.service';


@Component({
  selector: 'app-article-card',
  standalone: false,
  templateUrl: './article-card.html',
  styleUrl: './article-card.scss'
})
export class ArticleCard {

  @Input()
  public article!: ArticleWithoutContent;

  @Input()
  public canEdit: boolean = false;

  @Output()
  public editArticle: EventEmitter<number> = new EventEmitter<number>();

  @Output()
  public deleteArticle: EventEmitter<number> = new EventEmitter<number>();

  public titleImage!: string;

  // public article!: BlogArticle;

  public editMode: boolean = false;

  constructor(
    private langService: TranslateService,
    private authService: AuthenticationService) {
  }

  public get description() {
    const lang = this.langService.getCurrentLang();
    return this.article.description[lang];
  }

  public get title() {
    return this.article.title[this.langService.getCurrentLang()];
  }

  public get imagePosition() {
    return this.article.appearanceSettings.imagePosition;
  }

  public get image() {
    const fileName = this.article.appearanceSettings.titleImage;
    if (!isStringEmpty(fileName)) {
      return `${environment.backendUrl}/images/article/${this.article.id}/${fileName}?time=${this.article.lastChange}`;
    }
    return null;
  }

  delete(event: Event) {
    event.stopPropagation();
    this.deleteArticle.emit(this.article.id);
  }

  edit() {
    this.editArticle.emit(this.article.id);
  }

  get isLoggedIn() {
    return this.authService.isLoggedIn;
  }

  get localeId() {
    return localeIdMapping[this.langService.getCurrentLang()]! as string;
  }

  get timezoneOffset() {
    return Intl.DateTimeFormat().resolvedOptions().timeZone
  }

}
