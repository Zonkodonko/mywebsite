import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import environment from '../../environment';
import {tap} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private _isLoggedIn: boolean = false;

  constructor(private http: HttpClient) {
    const loginStatus = sessionStorage.getItem('login');
    if (loginStatus != null) {
      this._isLoggedIn = true;
    }
  }

  get isLoggedIn(): boolean {
    return this._isLoggedIn;
  }

  login(password: string, username: string) {
    return this.http.post(`${environment.backendUrl}/auth/login`,
      {
        username: username,
        password: password,
      },
      {
        headers: {
          'Content-Type': 'application/json',
        }
      }
    ).pipe(
      tap(response => {
        this._isLoggedIn = true;
        sessionStorage.setItem('login', JSON.stringify(response as any));
      }));
  }

  logout() {
    return this.http.post(`${environment.backendUrl}/auth/logout`, {}, {headers: this.getAuthHeaders(true)}).subscribe({
      complete: () => {
        this.invalidateSessionLocal();
      },
      error: (error) => {
        this.invalidateSessionLocal()
      }
    });
  }

  /**
   * Remove session token from local storage.
   */
  public invalidateSessionLocal() {
    this._isLoggedIn = false;
    sessionStorage.removeItem('login');
  }

  public getAuthHeaders(withRefresh: boolean = false): HttpHeaders {
    const jwt = JSON.parse(sessionStorage.getItem('login')!);
    const bearer = jwt.access_token;
    let headers = new HttpHeaders({'Authorization': `Bearer ${bearer}`})
    if(withRefresh) {
      headers = headers.set('refresh_token', jwt.refresh_token);
    }
    return headers;
  }

}
