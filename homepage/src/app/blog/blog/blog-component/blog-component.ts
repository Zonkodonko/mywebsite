import {Component, Input, OnInit} from '@angular/core';
import {BlogService} from '../../services/blog-service';
import {ArticleCreationData, BlogArticle, BlogArticleRaw, Topic, TopicRaw} from '../../data/BlogTypes';
import {TranslateService} from '@ngx-translate/core';
import {marked} from 'marked';
import environment from '../../../../environment';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ArticleDialog} from '../../article-dialog/article-dialog';
import {AuthenticationService} from '../../../authentication/authentication.service';
import {findAndDelete} from '../../../shared/utils/utils';
import {ConfirmDialog} from '../../../shared/components/confirm-dialog/confirm-dialog';

@Component({
  selector: 'app-blog-component',
  standalone: false,
  templateUrl: './blog-component.html',
  styleUrl: './blog-component.scss',
})
export class BlogComponent implements OnInit{

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
  private creating?: ArticleCreationData;

  constructor(private blogService: BlogService,
              private langService: TranslateService,
              private modalService: NgbModal,
              private authService: AuthenticationService) {
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

  ngOnInit(): void {
  }



  get isLoggedIn() {
    return this.authService.isLoggedIn;
  }

  updateArticles() {
    const lang = this.langService.getCurrentLang();
    this.articles = this.articlesRaw.map(raw => {
      return {
        ...raw,
        title: raw.title[lang],
        content: raw.content[lang],
        id: Number(raw.id)
      }
    })
  }

  deleteArticle(id: number) {
    this.modalService.open(ConfirmDialog, {centered: true, size: 'sm'}).closed.subscribe(confirm => {
      this.blogService.deleteArticle(id).subscribe(()=> {
        findAndDelete(this.articlesRaw, a => a.id === id);
        this.updateArticles();
      });
    })
  }

  public openArticleDialog(id: number | undefined = undefined) {
    const modalRef = this.modalService.open(ArticleDialog, {centered: true, size: 'lg', backdrop: 'static'});
    if(id !== undefined) {
      modalRef.componentInstance.article = this.articlesRaw.find(a => a.id === id);
    } else {
      if(this.creating !== undefined) {
        modalRef.componentInstance.article = this.creating;
      }
    }

    modalRef.closed.subscribe((result: ArticleCreationData) => {
      const newArticle = {topic: this._topicId, ...result};
      if(id !== undefined) {
        this.blogService.updateArticle({id:id,...newArticle}).subscribe( () => {
          Object.assign(this.articlesRaw.find(a => a.id == id)!,newArticle);
          this.updateArticles();
        });
      } else {
        this.blogService.createArticle(newArticle).subscribe(id => {
          this.articlesRaw.push({id: Number(id), ...newArticle,lastChange: new Date().getDate(), created: new Date().getTime() });
          this.updateArticles();
          this.creating = undefined;
        },
        error => {
          this.creating = result;
        })
      }
    })
  }

}
