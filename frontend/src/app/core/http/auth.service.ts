import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { SignUpRequest } from '../../shared/model/signup-request';
import { SignInRequest } from '../../shared/model/signin-request';
import { SignInResponse } from '../../shared/model/signin-resposne';

const authenticatedUserKey = 'authenticatedUser';
const url = "/api/auth"

@Injectable({
  providedIn: 'root'
})
export class AuthService{

  constructor(protected http: HttpClient) {

  }

  signup(newUser: SignUpRequest): Observable<string> {
    return this.http.post(`${url}/signup`, newUser, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': '*/*, application/xml, application/json'
      }),
      responseType: 'text'
    }).pipe(
      catchError(this.handleError<string>())
    )
  }

  signin(user: SignInRequest): Observable<SignInResponse> {
    return this.http.post<SignInResponse>(`${url}/signin`, user).pipe(
      tap(res => {
        localStorage.setItem(authenticatedUserKey, JSON.stringify({
          id: res.id,
          username: res.username,
          role: res.role
        }));
      }),
      catchError(this.handleError<SignInResponse>())
    );
  }

  signout(): Observable<void> {
    this.clearStorage();
    return this.http.post<void>(`${url}/signout`, null).pipe(
      catchError(this.handleError<void>())
    );
  }

  clearStorage(): void {
    localStorage.removeItem(authenticatedUserKey);
  }

  getAuthenticatedUser() {
    return JSON.parse(localStorage.getItem(authenticatedUserKey));
  }
  
  isAuthenticated(): boolean {
    return this.getAuthenticatedUser() != null;
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
