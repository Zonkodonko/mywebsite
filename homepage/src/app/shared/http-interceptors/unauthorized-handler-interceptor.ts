import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpStatusCode
} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {AuthenticationService} from '../../authentication/authentication.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {LogoutNotifcationModal} from '../logout-notifcation-modal/logout-notifcation-modal';

@Injectable()
export class UnauthorizedHandlerInterceptor implements HttpInterceptor {

  constructor(private authService: AuthenticationService, private modalService: NgbModal) {
  }

  /**
   * Invalidate session on 401 response.
   * @param req
   * @param next
   */
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      tap({
        error: (error) => {
          if (error.status === HttpStatusCode.Unauthorized) {
            this.authService.invalidateSessionLocal();
            this.modalService.open(LogoutNotifcationModal,{size: 'sm', centered: true});
          }
        }
      })
    );
  }



}
