import {Component, Input} from '@angular/core';
import {BlogArticle, BlogArticleRaw} from '../../../data/BlogTypes';
import {TranslateService} from '@ngx-translate/core';
import {marked} from 'marked';

@Component({
  selector: 'app-article-card',
  standalone: false,
  templateUrl: './article-card.html',
  styleUrl: './article-card.scss'
})
export class ArticleCard {

  @Input()
  public article?: BlogArticle;

  // public article!: BlogArticle;

  public editMode: boolean = false;

  constructor(private translateService: TranslateService, private authService: TranslateService ) {
    // translateService.onLangChange.subscribe(() => {
    //   this.updateArticle()
    // });
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

}
