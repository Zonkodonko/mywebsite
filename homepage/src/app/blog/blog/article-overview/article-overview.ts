import {Component, Input, OnInit} from '@angular/core';
import {BlogArticleRaw , TopicRaw} from '../../data/BlogTypes';
import {BlogService} from '../../services/blog-service';
import {TranslateService} from '@ngx-translate/core';
import {marked} from 'marked';

@Component({
  selector: 'app-article-overview',
  standalone: false,
  templateUrl: './article-overview.html',
  styleUrl: './article-overview.scss'
})
export class ArticleOverview implements OnInit{

  @Input()
  public topic!: TopicRaw;

  rawArticles: BlogArticleRaw[] = [];

  constructor(private blogService: BlogService, private translateService: TranslateService) {
  }

  ngOnInit(): void {
    this.blogService.getArticles(this.topic.id).subscribe(articles =>
      this.rawArticles = articles
    );
  }

  get title() {
    return this.topic.title[this.translateService.getCurrentLang()] ?? this.topic.title['en'];
  }

  get description() {
    return marked.parse(this.topic.description[this.translateService.getCurrentLang()] ?? this.topic.description['en'],{async:false})
  }






}
