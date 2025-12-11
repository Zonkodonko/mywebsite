import {Injectable} from '@angular/core';
import environment from '../../../../environment';
import {NewTopic, TopicRaw} from '../../data/BlogTypes';
import {HttpClient} from '@angular/common/http';
import {AuthenticationService} from '../../../authentication/authentication.service';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TopicApi {


  private readonly url = `${environment.backendUrl}/blog/topic`


  constructor(private http: HttpClient, private authService: AuthenticationService) {
  }

  /**
   * Create new topic.
   * @returns id of created topic.
   * @param topic
   */
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

    return this.http.post(`${this.url}`, formData, {
      headers: this.authService.getAuthHeaders(),
      responseType: 'text'
    });
  }

  getTopic(topic: string) {
    return this.http.get<TopicRaw>(`${this.url}/${topic}`);
  }

  getTopics(): Observable<TopicRaw[]> {
    return this.http.get<TopicRaw[]>(`${this.url}/all`);
  }

  deleteTopic(topic: string) {
    return this.http.delete(`${this.url}/${topic}`, {headers: this.authService.getAuthHeaders()});
  }
}
