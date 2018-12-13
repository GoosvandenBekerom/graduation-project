import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AUTHORIZATION_TOKEN_PREFIX, HEADER_AUTHORIZATION, KEY_AUTH_TOKEN} from 'core/constants';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem(KEY_AUTH_TOKEN);

    if (!token) { return next.handle(req); }

    const cloned = req.clone({
      headers: req.headers.set(HEADER_AUTHORIZATION, `${AUTHORIZATION_TOKEN_PREFIX} ${token}`)
    });

    return next.handle(cloned);
  }
}
