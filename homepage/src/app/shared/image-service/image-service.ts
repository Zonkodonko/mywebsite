import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import environment from '../../../environment';
import {from, Observable, switchMap} from 'rxjs';

export type Image = {
  filename: string,
  fileData: Blob
}

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private httpClient: HttpClient) {
  }

  /**
   * Get for entity
   * @param type entity type
   * @param id id of entity
   * @param lastChange
   */
  getImageFor(id: string|number, type: "topic" | "article" = "topic", lastChange: number = Date.now()): Observable<Image> {
    return this.httpClient.get(`${environment.backendUrl}/images/${type}/${id}?time=${lastChange}`, {
      observe: 'response',
      responseType: 'blob'
    })
      .pipe(
        switchMap((response, i) => {
          const blob = response.body!;
          let arrayBufferPromise = blob.arrayBuffer();
          const imagePromise = arrayBufferPromise.then(ab => {
            return {
              filename: response.headers.get('Content-Disposition')!.split('filename=')[1].replace(/"/g, ''),
              fileData: blob
            } as Image
          })
          return from(imagePromise);
        })
      )
  }


}
