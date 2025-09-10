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
    if(loginStatus != null) {
      this._isLoggedIn = true;
    }
    console.log('is logged in:' + this._isLoggedIn);
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
        console.log(JSON.stringify(response));
        this._isLoggedIn = true;
        sessionStorage.setItem('login', (response as any).access_token);
    }));
  }

  public getAuthHeaders(): HttpHeaders {
    const token = sessionStorage.getItem('login')!;
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

}
