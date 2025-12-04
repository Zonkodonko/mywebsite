import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BlogArticleRaw} from '../../../data/BlogTypes';
import {TranslateService} from '@ngx-translate/core';
import {marked} from 'marked';
import environment from '../../../../../environment';
import {DomSanitizer} from '@angular/platform-browser';


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

  // public article!: BlogArticle;

  public editMode: boolean = false;

  constructor(private translateService: TranslateService,
              private authService: TranslateService,
              private langService: TranslateService,
              private sanitizer: DomSanitizer) {
  }

  // private updateArticle() {
  //   if(this.articleRaw) {
  //     this.article = {
  //       ...this.articleRaw,
  //       title: this.articleRaw.title[this.translateService.getCurrentLang()],
  //       content: marked.parse(this.articleRaw.content[this.translateService.getCurrentLang()],{async: false})
  //     }
  //   }
  // }

  public get content() {
    const lang = this.langService.getCurrentLang();
    return this.sanitizer.bypassSecurityTrustHtml(marked.parse(this.article.content[lang], {async: false}));
  }

  public get title() {
    return this.article.title[this.langService.getCurrentLang()];
  }

  public get imagePosition() {
    return this.article.appearanceSettings.imagePosition;
  }

  public get image() {
    return `${environment.backendUrl}/images/article/${this.article.id}?time=${this.article.lastChange}`;
  }

  delete() {
    this.deleteArticle.emit(this.article);
  }

  edit() {
    this.editArticle.emit(this.article);
  }

  get locale() {
    return this.translateService.getCurrentLang();
  }

  get timezoneOffset() {
    return Intl.DateTimeFormat().resolvedOptions().timeZone
  }

}
