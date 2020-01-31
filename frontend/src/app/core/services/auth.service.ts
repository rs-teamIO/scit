import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpService } from './http/http.service';
import { Router } from '@angular/router';

const authenticatedUserKey = 'authenticatedUser';
const url = '/api/v1/users';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    protected httpService: HttpService,
    private router: Router
    ) {}


  userToXml(firstName: string, lastName: string, username: string, password: string, email: string) {
    const usernameXml = `<user:username>${username}</user:username>`;
    const passwordXml = `<user:password>${password}</user:password>`;
    const emailXml =    `<user:email>${email}</user:email>`;
    const firstNameXml = `<user:first_name>${firstName}</user:first_name>`;
    const lastNameXml = `<user:last_name>${lastName}</user:last_name>`;
    const user =  `<?xml version="1.0" encoding="UTF-8"?>` +
    `<user:user xmlns:user="http://www.scit.org/schema/user">` +
    usernameXml +
    passwordXml +
    emailXml +
    firstNameXml +
    lastNameXml +
    `</user:user>`;
    const parser = new DOMParser();
    const userXml = parser.parseFromString(user, 'application/xml');
    // console.log(userXml);
    return userXml;
  }

  signup(firstName: string, lastName: string, username: string, password: string, email: string) {
    const user = this.userToXml(firstName, lastName, username, password, email);

    this.httpService.post(`${url}`, user).toPromise()
    .then( response => {
      // this.httpService.handleSuccess(response);
      this.router.navigateByUrl('signin');
    })
    .catch( response => {
      this.httpService.handleError(response);
    });
  }


  // signin(user: SignInRequest): Observable<SignInResponse> {
  //   return this.http.post<SignInResponse>(`${url}/signin`, user).pipe(
  //     tap(res => {
  //       localStorage.setItem(authenticatedUserKey, JSON.stringify({
  //         id: res.id,
  //         username: res.username,
  //         role: res.role
  //       }));
  //     }),
  //     catchError(this.handleError<SignInResponse>())
  //   );
  // }

  // signout(): Observable<void> {
  //   this.clearStorage();
  //   return this.http.post<void>(`${url}/signout`, null).pipe(
  //     catchError(this.handleError<void>())
  //   );
  // }

  clearStorage(): void {
    localStorage.removeItem(authenticatedUserKey);
  }

  getAuthenticatedUser() {
    return JSON.parse(localStorage.getItem(authenticatedUserKey));
  }

  isAuthenticated(): boolean {
    return this.getAuthenticatedUser() != null;
  }



}
