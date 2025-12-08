import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import environment from '../../../environment';
import {from, Observable, of, switchMap} from 'rxjs';
import * as JSZip from 'jszip';

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

  getAllImagesForArticle(id: number, lastChange: number): Observable<Image[]> {
    return this.httpClient.get(`${environment.backendUrl}/images/article/${id}/all?time=${lastChange}`, {
      observe: "response",
      responseType: "arraybuffer"
    })
      .pipe(
        switchMap(async (data) => {
          // JSZip load data
          if(data.body == null || data.body.byteLength === 0) {
            return [] as Image[]
          }
          const zip = await JSZip.loadAsync(data.body! as ArrayBuffer);
          const filePromises: Promise<Image>[] = [];

          // Extract file from zip entries
          zip.forEach((relativePath, zipEntry) => {
            if (!zipEntry.dir) {
              const promise = zipEntry.async('blob').then(blob => {
                return {
                  filename: zipEntry.name,
                  fileData: blob
                } as Image;
              });
              filePromises.push(promise);
            }
          });
          return Promise.all(filePromises);
        })
      );
  }


}
