import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { HttpService } from './http.service';
import { Observable } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';

const tokenType = `Bearer`;

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {
  constructor(
    public auth: HttpService,
    private cookieService: CookieService
    ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const tokenValue = this.cookieService.get(tokenType);
    if (tokenValue) {
      request = request.clone({
        setHeaders: {
          Authorization: `${tokenType} ${tokenValue}`,
          'Content-Type': 'application/xml'
        }
      });
    } else {
      request = request.clone({
        setHeaders: {
          'Content-Type': 'application/xml'
        }
      });
    }
    return next.handle(request);
  }
}
