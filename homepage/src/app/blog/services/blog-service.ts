import {Injectable} from '@angular/core';
import {BlogArticleRaw, NewTopic, TopicRaw} from '../data/BlogTypes';
import environment from '../../../environment';
import {HttpClient} from '@angular/common/http';
import {AuthenticationService} from '../../authentication/authentication.service';
import {TranslateService} from '@ngx-translate/core';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BlogService {

  private readonly url = `${environment.backendUrl}/blog`


  constructor(private http: HttpClient, private authService: AuthenticationService, private langService: TranslateService) {
  }


  getArticles(topic: string): Observable<BlogArticleRaw[]> {
    return this.http.get<BlogArticleRaw[]>(`${this.url}/articles/${topic}`)
  }

  getTopics(): Observable<TopicRaw[]> {
    return this.http.get<TopicRaw[]>(`${this.url}/topics`);
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

    return this.http.post(`${this.url}/topic`, formData, {headers: this.authService.getAuthHeaders(), responseType: 'text'});
  }


}
