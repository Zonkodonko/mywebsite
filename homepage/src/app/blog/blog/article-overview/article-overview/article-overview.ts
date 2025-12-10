import {Component, Input} from '@angular/core';
import {marked} from 'marked';
import environment from '../../../../../environment';
import {BlogService} from '../../../services/blog-service';
import {TranslateService} from '@ngx-translate/core';
import {
  ArticleCreationData,
  ArticleWithoutContent,
  BlogArticle,
  BlogArticleRaw, BlogArticleWithoutContent,
  EditArticle,
  Topic,
  TopicRaw
} from '../../../data/BlogTypes';
import {AuthenticationService} from '../../../../authentication/authentication.service';
import {ArticleDialog} from '../../../article-dialog/article-dialog';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConfirmDialog} from '../../../../shared/components/confirm-dialog/confirm-dialog';
import {findAndDelete} from '../../../../shared/utils/utils';
import {ArticleApi} from '../../../services/article-service/article-api';

@Component({
  selector: 'app-article-overview',
  standalone: false,
  templateUrl: './article-overview.html',
  styleUrl: './article-overview.scss'
})
export class ArticleOverview {

  private _topicId: string = "";
  private topicRaw?: TopicRaw;
  public topic: Topic = {
    id: "",
    title: "Loading...",
    description: " Loading...",
    image: ""
  }
  public articlesRaw: ArticleWithoutContent[] = [];
  public articlesDisplay: BlogArticleWithoutContent[] = [];
  private creating?: ArticleCreationData;

  constructor(private blogService: BlogService,
              private langService: TranslateService,
              private authService: AuthenticationService,
              private modalService: NgbModal,
              private articleService:ArticleApi) {
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
        this.updateArticles();
      }
    );
  }

  updateArticles() {
    const lang = this.langService.getCurrentLang();
    this.articlesDisplay = this.articlesRaw.map(raw => {
      return {
        ...raw,
        title: raw.title[lang],
        description: raw.description[lang],
        id: Number(raw.id)
      }
    })
      .sort((a, b) => b.created ?? 0 - a.created ?? 0);
  }

  get isLoggedIn() {
    return this.authService.isLoggedIn;
  }


  deleteArticle(id: number) {
    this.modalService.open(ConfirmDialog, {centered: true, size: 'sm'}).closed.subscribe(confirm => {
      this.articleService.deleteArticle(id).subscribe(()=> {
        findAndDelete(this.articlesRaw, a => a.id === id);
        this.updateArticles();
      });
    })
  }

  public openArticleDialog(id: number | undefined = undefined) {
    const modalRef = this.modalService.open(ArticleDialog, {centered: true, size: 'lg', backdrop: 'static'});
    if(id !== undefined) {
      modalRef.componentInstance.article = {...this.articlesRaw.find(a => a.id === id)!};
    } else {
      if(this.creating !== undefined) {
        modalRef.componentInstance.article = this.creating;
      }
    }

    modalRef.closed.subscribe((result: ArticleCreationData | EditArticle) => {
      const newArticle = {topic: this._topicId, ...result};
      if(id !== undefined) {
        this.articleService.updateArticle({id:id,...newArticle}).subscribe( () => {
          Object.assign(this.articlesRaw.find(a => a.id == id)!,{lastChange: Date.now(),...newArticle});
          this.updateArticles();
        });
      } else {
        this.articleService.createArticle(newArticle).subscribe(id => {
            this.articlesRaw.push({
              id: Number(id),
              lastChange: Date.now(),
              created: Date.now(),
              ...newArticle
            });
            this.updateArticles();
            this.creating = undefined;
          },
          error => {
            const {images, ...data} = result;
            this.creating = data as ArticleCreationData;
          })
      }
    })
  }
}
