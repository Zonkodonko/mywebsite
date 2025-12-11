import {Component, OnDestroy, OnInit} from '@angular/core';
import {BlogArticleRaw, EditArticle} from '../../data/BlogTypes';
import {TranslateService} from '@ngx-translate/core';
import {DomSanitizer, SafeHtml} from '@angular/platform-browser';
import {marked} from 'marked';
import {getArticleRenderer} from '../../../shared/utils/marked-extension';
import {BlogService} from '../../services/blog-service';
import {ActivatedRoute} from '@angular/router';
import {AuthenticationService} from '../../../authentication/authentication.service';
import {ArticleApi} from '../../services/article-service/article-api';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ArticleDialog} from '../../article-dialog/article-dialog';
import {Subscription} from 'rxjs';
import {TopicApi} from '../../services/topic-service/topic-api';
import {LocalizedText} from '../../../shared/translation/LocalizedText';

@Component({
  selector: 'app-article-component',
  standalone: false,
  templateUrl: './article-component.html',
  styleUrl: './article-component.scss'
})
export class ArticleComponent implements OnInit, OnDestroy {

  public article!: BlogArticleRaw;

  public title: string = 'loading';
  public content: string | SafeHtml = 'loading';

  private languageSubscription!: Subscription;

  private topicNames: LocalizedText | undefined = undefined;
  public topic:string = "loading...";

  constructor(
    private authService: AuthenticationService,
    private langService: TranslateService,
    private sanitizer: DomSanitizer,
    private blogService: BlogService,
    private activatedRoute: ActivatedRoute,
    private articleService: ArticleApi,
    private modalService: NgbModal,
    private topicService: TopicApi
  ) {
  }

  /**
   * Get article id from route params and load article from backend.
   * Trigger visual update when article is loaded.
   */
  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe(params => {
      const topic: string = params.get('topicId')!;
      const articleName: string = params.get('article')!;
      this.blogService.getArticleByName(topic, articleName).subscribe(article => {
        this.article = article;
        this.fetchTopicNames();
        this.updateDisplayData();
      });
    });


    this.languageSubscription = this.langService.onLangChange.subscribe(() => {
      if(this.article != undefined){ //should not happen, but sometime language changes before article is loaded
        console.log("lang changed");
        this.updateDisplayData();
      }
    });
  }

  ngOnDestroy(): void {
    this.languageSubscription.unsubscribe();
  }

  /**
   * If the topic name is not yet cached, it will be fetched from the backend.
   */
  private fetchTopicNames() {
    this.topicNames = this.blogService.topicNameCache.get(this.article.topic);
    if(this.topicNames == undefined){
      this.topicService.getTopic(this.article.topic).subscribe((topic) => {
        this.blogService.topicNameCache.set(this.article.topic, topic.title);
        this.topicNames = topic.title;
        this.topic = this.topicNames[this.langService.getCurrentLang()]!;
      });
    }
  }


  /**
   * Updates the displayed data according to language.
   */
  updateDisplayData() {
    const lang = this.langService.getCurrentLang();
    console.log("current lang: " + lang);
    this.title = this.article.title[lang];
    marked.use({renderer: getArticleRenderer(this.article.id, this.article.lastChange)});
    this.content = this.sanitizer.bypassSecurityTrustHtml(marked.parse(this.article.content[lang], {async: false}));
    if(this.topicNames != undefined){
      this.topic = this.topicNames[lang];
    }
  }

  public openEditDialog() {
    const modalRef = this.modalService.open(ArticleDialog, {size: 'lg'});
    modalRef.componentInstance.article = this.article;

    modalRef.closed.subscribe((modified: EditArticle) => {
      const {images, imagesToDelete, ...rest} = modified;
      this.articleService.updateArticle(modified).subscribe(() => {
          Object.assign(this.article, rest);
          this.updateDisplayData();
        }
      )
    });
  }

  get canEdit() {
    return this.authService.isLoggedIn;
  }

  get locale() {
    return this.langService.getCurrentLang();
  }

  get timezoneOffset() {
    return Intl.DateTimeFormat().resolvedOptions().timeZone
  }


}
