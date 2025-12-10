import {Injectable} from '@angular/core';
import {BlogArticleRaw, FullTopic} from '../data/BlogTypes';
import environment from '../../../environment';
import {HttpClient} from '@angular/common/http';
import {AuthenticationService} from '../../authentication/authentication.service';
import {Observable, switchMap} from 'rxjs';
import {tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BlogService {

  private readonly url = `${environment.backendUrl}/blog`

  private topicsCache: Map<string, { id: number, names: string[] }[]> = new Map<string, {
    id: number,
    names: string[]
  }[]>();


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
            names.push(article.title[lang]);
          }
          return {id: article.id, names: names}
        })
        this.topicsCache.set(topic, articles);
      })
    );
  }

  /**
   * Get article by name. If topic is not cached, load topic first.
   * If topic is cached, get id from cache.
   * @param topic of article to get.
   * @param article name of article to get.
   * @returns Observable of BlogArticleRaw with article data.
   */
  getArticleByName(topic: string, article: string): Observable<BlogArticleRaw> {
    console.log("Getting article: " + topic + " " + article + "");
    console.log(this.topicsCache.get(topic));
    if (this.topicsCache.size === 0 || !this.topicsCache.has(topic)) {
      return this.getFullTopic(topic).pipe( // Load topics cache
        switchMap(() => this.getArticleByName(topic, article))
      );
    } else {
      const id = this.topicsCache.get(topic)!.find(t => t.names.includes(article))!.id;
      return this.http.get<BlogArticleRaw>(`${this.url}/article/${id}`);
    }
  }


}
