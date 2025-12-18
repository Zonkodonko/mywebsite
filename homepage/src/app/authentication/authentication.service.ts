import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import environment from '../../environment';
import {Subject, tap} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private _isLoggedIn: boolean = false;

  public logoutEvent: Subject<void> = new Subject<void>();
  public loginEvent: Subject<void> = new Subject<void>();
  public expireTime: number = 0;

  constructor(private http: HttpClient) {
    const loginStatus = sessionStorage.getItem('login');
    if (loginStatus != null && loginStatus != 'null') {
      this._isLoggedIn = true;
      this.expireTime = JSON.parse(loginStatus).expires_in as number;
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
      tap((response: any) => {
        this.safeJwtInfo(response);
        this.loginEvent.next();
      }));
  }

  /**
   * Refresh access token.
   * @returns Observable of new jwt info.
   */
  refreshToken() {
    return this.http.post(`${environment.backendUrl}/auth/refresh`, {}, {headers: this.getRefreshHeader()}).pipe(
      tap((response:any) => {
        this.safeJwtInfo(response);
      }));
  }

  /**
   * Save jwt info to local storage.
   * @param jwt jwt info.
   */
  private safeJwtInfo(jwt: any) {
    this._isLoggedIn = true;
    this.expireTime = Date.now() + (jwt.expires_in as number * 1000);
    sessionStorage.setItem('login', JSON.stringify({expireTime: this.expireTime, ...jwt}));
  }

  logout() {
    this.logoutEvent.next();
    return this.http.post(`${environment.backendUrl}/auth/logout`, {}, {headers: this.getRefreshHeader()}).subscribe({
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

  public getAuthHeaders(): HttpHeaders {
    const jwt = JSON.parse(sessionStorage.getItem('login')!);
    const bearer = jwt.access_token;
    return new HttpHeaders({'Authorization': `Bearer ${bearer}`});
  }

  private getRefreshHeader(): HttpHeaders {
    return new HttpHeaders({'refresh_token': JSON.parse(sessionStorage.getItem('login')!).refresh_token});
  }

}
