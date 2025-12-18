import {Injectable} from '@angular/core';
import {BlogArticleRaw, FullTopic} from '../data/BlogTypes';
import environment from '../../../environment';
import {HttpClient} from '@angular/common/http';
import {AuthenticationService} from '../../authentication/authentication.service';
import {Observable, switchMap} from 'rxjs';
import {tap} from 'rxjs/operators';
import slugify from 'slugify';
import {LocalizedText} from '../../shared/translation/LocalizedText';

@Injectable({
  providedIn: 'root'
})
export class BlogService {

  private readonly url = `${environment.backendUrl}/blog`

  private topicsCache: Map<string, { id: number, names: string[] }[]> = new Map<string, {
    id: number,
    names: string[]
  }[]>();

  public topicNameCache: Map<string, LocalizedText> = new Map<string, LocalizedText>();


  constructor(private http: HttpClient, private authService: AuthenticationService) {
  }

  /**
   * Get topic info and all related articles. Also, caches articles name-id mapping for routing via name
   * @param topic to get articles from.
   * @returns Observable of FullTopic with all articles.
   */
  getFullTopic(topic: string): Observable<FullTopic> {
    return this.http.get<FullTopic>(`${this.url}/topic/${topic}/articles`).pipe(
      tap((fullTopic: FullTopic) => {
        const articles = fullTopic.articles.map(article => {
          const names: string[] = [];
          for (let lang in article.title) {
            names.push(slugify(article.title[lang]));
          }
          return {id: article.id, names: names}
        })
        this.topicsCache.set(topic, articles);
        this.topicNameCache.set(topic, fullTopic.topic.title);
      })
    );
  }

  /**
   * Get article by name. If topic is not cached, load topic first.
   * If topic is cached, get id from cache.
   * @param topic of article to get.
   * @param article name of article to get. <strong>Must be slugified!</strong>.
   * @param throwError if true, throw error if article not found. If false, methode will be called again after a cache refresh. But this time with throwError = true.
   * @returns Observable of BlogArticleRaw with article data.
   */
  getArticleByName(topic: string, article: string, throwError: boolean = false): Observable<BlogArticleRaw> {
    let id: number | undefined;
    if (this.topicsCache.size > 0 && this.topicsCache.has(topic)) {
      const articleEntry = this.topicsCache.get(topic)!.find(t => t.names.includes(article));
      if (articleEntry != undefined) {
        id = articleEntry.id;
      }
    }
    if (id == undefined) {
      if (throwError) {
        throw new Error(`Article ${article} not found in topic ${topic}!`);
      } else {
        return this.getFullTopic(topic).pipe( // Load topics cache
          switchMap(() => this.getArticleByName(topic, article, true))
        );
      }
    }
    return this.http.get<BlogArticleRaw>(`${this.url}/article/${id}`);

  }


}
