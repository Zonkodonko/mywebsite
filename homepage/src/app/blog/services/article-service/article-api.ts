import {Injectable} from '@angular/core';
import environment from '../../../../environment';
import {ArticleCreationData, BlogArticleRaw, EditArticle, NewArticle} from '../../data/BlogTypes';
import {HttpClient} from '@angular/common/http';
import {AuthenticationService} from '../../../authentication/authentication.service';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ArticleApi {

  private readonly url = `${environment.backendUrl}/blog`


  constructor(private http: HttpClient, private authService: AuthenticationService) {
  }

  /**
   * Delete article with given id.
   * @param id of article to delete.
   */
  deleteArticle(id: number): Observable<any> {
    return this.http.delete(`${this.url}/article/${id}`, {headers: this.authService.getAuthHeaders()});
  }


  /**
   * Update article.
   * @param article to update.
   */
  updateArticle(article: EditArticle): Observable<any> {
    const {imagesToDelete, ...articleCreationData} = article;
    const formData = this.buildFormData(articleCreationData);
    if (imagesToDelete) {
      const imagesToDeleteBlob = new Blob([JSON.stringify(imagesToDelete)], {
        type: 'application/json'
      });
      formData.append('imagesToDelete', imagesToDeleteBlob);
    }
    return this.http.put(`${this.url}/article/${article.id}`, formData, {headers: this.authService.getAuthHeaders()});
  }

  /**
   * Create new article.
   * @param article to create.
   * @returns id of created article.
   */
  createArticle(article: NewArticle): Observable<string> {
    const formData = this.buildFormData(article);

    return this.http.post(`${this.url}/article`, formData, {
      headers: this.authService.getAuthHeaders(),
      responseType: 'text'
    })
  }

  getArticle(id: number) {
    return this.http.get<BlogArticleRaw>(`${this.url}/article/${id}`);
  }

  /**
   * Build form data for article creation. (Without images to delete)
   * @param data to build form data from.
   * @returns form data for article creation.
   */
  private buildFormData(data: ArticleCreationData): FormData {
    const {images, ...articleData} = data;
    const formData = new FormData();
    const articleBlob = new Blob([JSON.stringify(articleData)], {
      type: 'application/json'
    });
    formData.append('article', articleBlob);

    if (images) {
      for (const image of images) {
        formData.append('images', image, image.name);
      }
    }

    return formData;
  }

}
