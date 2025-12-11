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

@Component({
  selector: 'app-article-component',
  standalone: false,
  templateUrl: './article-component.html',
  styleUrl: './article-component.scss'
})
export class ArticleComponent implements OnInit, OnDestroy {

  public article!: BlogArticleRaw;

  // // editable fields
  // public contentInput!: string;
  // public titleInput!: string;
  // public imagesToDelete: string[] = [];
  // public imagesToAdd: File[] = [];
  //
  // //state
  // public isEditingTitle: boolean = false;
  // public isEditingContent: boolean = false;

  // article data
  public title: string = 'loading';
  public content: string | SafeHtml = 'loading';

  private languageSubscription!: Subscription;

  constructor(
    private authService: AuthenticationService,
    private langService: TranslateService,
    private sanitizer: DomSanitizer,
    private blogService: BlogService,
    private activatedRoute: ActivatedRoute,
    private articleService: ArticleApi,
    private modalService: NgbModal) {
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
        this.updateDisplayData();
      });
    })
    this.languageSubscription = this.langService.onLangChange.subscribe(() => {
      console.log("lang changed");
      this.updateDisplayData();
    });
  }

  ngOnDestroy(): void {
    this.languageSubscription.unsubscribe();
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

    // this.contentInput = this.article.content[lang];
    // this.titleInput = this.article.title[lang];
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


  // toggleEditContent() {
  //   const currentLang = this.langService.getCurrentLang();
  //   if(this.isEditingContent) {
  //     const copy = Object.assign({}, this.article);
  //     copy.content[currentLang] = this.contentInput;
  //     this.articleService.updateArticle({images: this.imagesToAdd, imagesToDelete: this.imagesToDelete, ...copy}).subscribe(() => {
  //       this.article.content[currentLang] = this.contentInput;
  //       this.imagesToAdd = [];
  //       this.imagesToDelete = [];
  //       this.updateDisplayData();
  //     });
  //   }
  //   this.isEditingContent = !this.isEditingContent;
  // }
  //
  // toggleEditTitle() {
  //   const currentLang = this.langService.getCurrentLang();
  //   if(this.isEditingContent) {
  //     const copy = Object.assign({}, this.article);
  //     copy.title[currentLang] = this.titleInput;
  //     this.articleService.updateArticle({images: [], imagesToDelete: [], ...copy}).subscribe(() => {
  //       this.article.title[currentLang] = this.titleInput;
  //       this.updateDisplayData();
  //     });
  //   }
  //   this.isEditingContent = !this.isEditingContent;
  // }


  get locale() {
    return this.langService.getCurrentLang();
  }

  get timezoneOffset() {
    return Intl.DateTimeFormat().resolvedOptions().timeZone
  }


}
