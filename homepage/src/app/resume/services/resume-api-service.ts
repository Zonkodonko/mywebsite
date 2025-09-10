import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Skill} from '../data/ResumeData';
import environment from '../../../environment';
import {Observable} from 'rxjs';
import {AuthenticationService} from '../../authentication/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class ResumeApiService {

  private readonly url = `${environment.backendUrl}/resume/`

  constructor(private http: HttpClient, private authService: AuthenticationService) {
  }

  /**
   * Save skillset to backend. (update, create and delete)
   * @param skillset to save
   */
  updateSkillset(skillset: Skill[]): Observable<any> {
    return this.http.post(`${this.url}skills`, skillset, {headers: this.authService.getAuthHeaders()});
  }

  fetchSkillset(): Observable<Skill[]> {
    return this.http.get<Skill[]>(`${this.url}skills`);
  }

}
