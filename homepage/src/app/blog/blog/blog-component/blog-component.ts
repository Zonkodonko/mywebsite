import {Component, Input} from '@angular/core';
import {BlogService} from '../../services/blog-service';
import {BlogArticle, BlogArticleRaw, Topic, TopicRaw} from '../../data/BlogTypes';
import {TranslateService} from '@ngx-translate/core';
import {marked} from 'marked';
import environment from '../../../../environment';

@Component({
  selector: 'app-blog-component',
  standalone: false,
  templateUrl: './blog-component.html',
  styleUrl: './blog-component.scss',
})
export class BlogComponent {

  private _topicId: string = "";
  private topicRaw?: TopicRaw;
  public topic: Topic = {
    id: "",
    title: "Loading...",
    description: " Loading...",
    image: ""
  }
  public articlesRaw: BlogArticleRaw[] = [];
  public articles: BlogArticle[] = [];

  constructor(private blogService: BlogService, private langService: TranslateService) {
  }

  @Input()
  set topicId(id: string) {
    this._topicId = id;
    const lang = this.langService.getCurrentLang();
    this.blogService.getFullTopic(id).subscribe(
      blog => {
        this.topicRaw = blog.topic;
        this.topic = {
          ...blog.topic,
          title: blog.topic.title[lang],
          description: marked.parse(blog.topic.description[lang], {async: false}),
          image: `${environment.backendUrl}/images/topic/${id}`
        }
        this.articlesRaw = blog.articles;
        this.articles = blog.articles.map(raw => {
          return {
            ...raw,
            title: raw.title[lang],
            content: raw.content[lang],
          }
        })
      }
    )
  }

}
