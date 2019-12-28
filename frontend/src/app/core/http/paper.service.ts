import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

const url = "/api/papers"


@Injectable({
  providedIn: 'root'
})
export class PaperService {

  constructor(
    protected http: HttpClient
  ) { }

  preview(xml: string): Observable<string> {
    return this.http.post(`${url}/preview`, xml, {
      headers: new HttpHeaders({
        'Content-Type': 'application/xml',
        'Accept': '*/*, application/xml, application/json'
      }),
      responseType: 'text'
    }).pipe(
      catchError(this.handleError<string>())
    );
  }

  protected handleError<E>(operation = 'operation', result?: E) {
    return (response: any): Observable<E> => {
      console.error(response);
      if (response.error) {
        console.log(response.error);
      } else {
        console.log('Client side error!');
      }
      return Observable.throw(result as E);
    };
  }
}
