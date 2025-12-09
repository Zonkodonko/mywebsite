import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BlogArticleRaw} from '../../../data/BlogTypes';
import {TranslateService} from '@ngx-translate/core';
import {marked, Renderer} from 'marked';
import environment from '../../../../../environment';
import {DomSanitizer} from '@angular/platform-browser';
import {isStringEmpty} from '../../../../shared/utils/utils';
import {getArticleRenderer} from '../../../../shared/utils/marked-extension';
import {localeIdMapping} from '../../../../shared/translation/Translation';


@Component({
  selector: 'app-article-card',
  standalone: false,
  templateUrl: './article-card.html',
  styleUrl: './article-card.scss'
})
export class ArticleCard {

  @Input()
  public article!: BlogArticleRaw;

  @Input()
  public canEdit: boolean = false;

  @Output()
  public editArticle: EventEmitter<BlogArticleRaw> = new EventEmitter<BlogArticleRaw>();

  @Output()
  public deleteArticle: EventEmitter<BlogArticleRaw> = new EventEmitter<BlogArticleRaw>();

  public titleImage!: string;

  // public article!: BlogArticle;

  public editMode: boolean = false;

  constructor(
              private langService: TranslateService,
              private sanitizer: DomSanitizer) {
  }

  public get content() {
    const lang = this.langService.getCurrentLang();
    marked.use({renderer: getArticleRenderer(this.article.id!, this.article.lastChange!)})
    return this.sanitizer.bypassSecurityTrustHtml(marked.parse(this.article.content[lang], {async: false}));
  }

  public get title() {
    return this.article.title[this.langService.getCurrentLang()];
  }

  public get imagePosition() {
    return this.article.appearanceSettings.imagePosition;
  }

  public get image() {
    const fileName = this.article.appearanceSettings.titleImage;
    if(!isStringEmpty(fileName)) {
      return `${environment.backendUrl}/images/article/${this.article.id}/${fileName}?time=${this.article.lastChange}`;
    }
    return null;
  }

  delete() {
    this.deleteArticle.emit(this.article);
  }

  edit() {
    this.editArticle.emit(this.article);
  }

  get localeId() {
    return localeIdMapping[this.langService.getCurrentLang()]! as string;
  }

  get timezoneOffset() {
    return Intl.DateTimeFormat().resolvedOptions().timeZone
  }

}
