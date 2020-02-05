import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { NgxXml2jsonService } from 'ngx-xml2json';
import Swal from 'sweetalert2';
import { BehaviorSubject } from 'rxjs';

const getAllAuthorsURL = '/api/v1/users/authors';
const recomendedAuthorsUrl = '/api/v1/paper/recommended-authors?paper_id=http://www.scit.org/papers/';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private authorsOb = new BehaviorSubject<any[]>([]);
  private authorsHolder: any[] = [];
  authors = this.authorsOb.asObservable();

  constructor(
    protected http: HttpClient,
    private router: Router,
    private parser: NgxXml2jsonService
  ) { }


  getAllAuthors(id: string) {
    this.http.get(`${recomendedAuthorsUrl + id}`, {
      headers: new HttpHeaders({
        'Content-Type': 'application/xml',
        Accept: '*/*, application/xml, application/json'
      }),
      responseType: 'text'
    }).toPromise()
    .then(
      response => {
        const xml = new DOMParser().parseFromString(response, `text/xml`);
        const responseInJson: any = this.parser.xmlToJson(xml);
        delete responseInJson.response.users[`#text`];
        if (!responseInJson.response.users.user.length) {
          const holder = [];
          holder.push(responseInJson.response.users.user);
          this.authorsHolder = holder;
          this.authorsOb.next(this.authorsHolder);
        } else {
          this.authorsHolder = responseInJson.response.users.user;
          this.authorsOb.next(this.authorsHolder);
        }

      }
    )
    .catch(
      response => this.handleError(response)
    );
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


}
