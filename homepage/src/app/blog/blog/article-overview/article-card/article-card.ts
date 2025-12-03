import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BlogArticle, BlogArticleRaw} from '../../../data/BlogTypes';
import {TranslateService} from '@ngx-translate/core';
import {marked} from 'marked';
import environment from '../../../../../environment';

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
              private langService: TranslateService) {
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
    return marked.parse(this.article.content[lang],{async: false});
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

}
