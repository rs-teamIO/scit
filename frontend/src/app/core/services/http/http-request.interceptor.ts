import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { HttpService } from './http.service';
import { Observable } from 'rxjs';
@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {
  constructor(public auth: HttpService) {}

  // ${this.auth.getToken()}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer `,
        'Content-Type': 'application/xml'
      }
    });
    return next.handle(request);
  }
}
