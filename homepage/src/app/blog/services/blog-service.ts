import {Injectable} from '@angular/core';
import {BlogArticleRaw, FullTopic, NewTopic, TopicRaw} from '../data/BlogTypes';
import environment from '../../../environment';
import {HttpClient} from '@angular/common/http';
import {AuthenticationService} from '../../authentication/authentication.service';
import {Observable, of} from 'rxjs';
import {tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BlogService {

  private readonly url = `${environment.backendUrl}/blog`

  private topicsCache: TopicRaw[] = [];


  constructor(private http: HttpClient, private authService: AuthenticationService) {
  }


  getArticles(topic: string): Observable<BlogArticleRaw[]> {
    return this.http.get<any[]>(`${this.url}/articles/${topic}`)
  }

  getFullTopic(topic: string): Observable<FullTopic> {
    return this.http.get<FullTopic>(`${this.url}/topic/${topic}`);
  }

  getTopics(): Observable<TopicRaw[]> {
    return this.http.get<TopicRaw[]>(`${this.url}/topics`).pipe(
      tap(topics => this.topicsCache = topics)
    );
  }

  createTopic(topic: NewTopic) {
    const formData = new FormData();
    const {image, ...topicData} = topic;

    const topicBlob = new Blob([JSON.stringify(topicData)], {
      type: 'application/json'
    });
    formData.append('topic', topicBlob);

    if (image) {
      formData.append('image', image, image.name);
    }

    return this.http.post(`${this.url}/topic`, formData, {
      headers: this.authService.getAuthHeaders(),
      responseType: 'text'
    });
  }


}
