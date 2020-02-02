import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';

import Swal from 'sweetalert2';

const signUpUrl = '/api/v1/users';
const signInUrl = '/api/login';

const tokenType = `Bearer`;



@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private router: Router
    ) {}


  userToXml(firstName: string, lastName: string, username: string, password: string, email: string): string {
    const usernameXml = `<user:username>${username}</user:username>`;
    const passwordXml = `<user:password>${password}</user:password>`;
    const emailXml =    `<user:email>${email}</user:email>`;
    const firstNameXml = `<user:first_name>${firstName}</user:first_name>`;
    const lastNameXml = `<user:last_name>${lastName}</user:last_name>`;
    const user = `<user:user xmlns:user="http://www.scit.org/schema/user">` +
    usernameXml +
    passwordXml +
    emailXml +
    firstNameXml +
    lastNameXml +
    `</user:user>`;
    return user;
  }

  authToXml(username: string, password: string): string {
    const usernameXml = `<username>${username}</username>`;
    const passwordXml = `<password>${password}</password>`;
    const auth = `<login xmlns="http://www.scit.org/schema/login">` +
    usernameXml +
    passwordXml +
    `</login>`;
    return auth;
  }

  signup(firstName: string, lastName: string, username: string, password: string, email: string) {
    const user = this.userToXml(firstName, lastName, username, password, email);

    this.http.post(`${signUpUrl}`, user, {observe: 'response'}).toPromise()
    .then( response => {
      response.headers.get('Location');
      Swal.fire(
        'Welcome!',
        'You have signed up successfully.',
        'success'
      )
      .then(
        () => this.router.navigateByUrl('signin')
      );
    })
    .catch( response => {
      this.handleError(response);
    });
  }
  signin(username: string, password: string) {
    const auth = this.authToXml(username, password);

    this.http.post<any>(`${signInUrl}`, auth).toPromise()
    .then( response => {
      this.cookieService.set(tokenType, response.token);
      Swal.fire(
        'Welcome!',
        'You have signed in successfully.',
        'success'
      )
      .then(
        () => this.router.navigateByUrl('new-paper')
      );
    })
    .catch( response => {
      this.handleError(response);
    });
  }

  signout() {
    this.cookieService.deleteAll();
  }


  handleError(response: any) {
    if (response.error.body) {
      Swal.fire(
        'Oops...',
        'Client side error!',
        'error'
      );
    } else {
      Swal.fire(
        'Oops...',
        response.error.message,
        'error'
      );
    }
  }

  isAuthenticated(): boolean {
    return this.cookieService.get(tokenType) ? true : false;
  }



  // clearStorage(): void {
  //   localStorage.removeItem(authenticatedUserKey);
  // }

  // getAuthenticatedUser() {
  //   return JSON.parse(localStorage.getItem(authenticatedUserKey));
  // }





}
