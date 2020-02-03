import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { NgxXml2jsonService } from 'ngx-xml2json';
import Swal from 'sweetalert2';

const myReviewRequestsUrl = '/api/v1/papers/assigned';
const acceptRequestUrl = '/api/v1/review/accept?paper_id=';
const declineRequestUrl = '/api/v1/review/decline?paper_id=';
const forReviewUrl = '/api/v1/papers/in-review';


@Injectable({
  providedIn: 'root'
})
export class ReviewService {


  private requestsOb = new BehaviorSubject<any[]>([]);
  private requestsHolder: any[] = [];
  requests = this.requestsOb.asObservable();

  constructor(
    protected http: HttpClient,
    private router: Router,
    private parser: NgxXml2jsonService
  ) { }


  getReviewRequest() {
    this.http.get(`${myReviewRequestsUrl}`, {
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
        delete responseInJson.response.papers[`#text`];
        if (!responseInJson.response.papers.paper.length) {
          const holder = [];
          holder.push(responseInJson.response.papers.paper);
          this.requestsHolder = holder;
          this.requestsOb.next(this.requestsHolder);
        } else {
          this.requestsHolder = responseInJson.response.papers.paper;
          this.requestsOb.next(this.requestsHolder);
        }

      }
    )
    .catch(
      response => this.handleError(response)
    );
  }

  accept(request: any) {
    this.http.put(`${acceptRequestUrl + request.id}`, null).toPromise()
        .then(() => {
          Swal.fire(
            'Good job!',
            'You have accepted request for paper: ' + request.title,
            'success'
          )
          .then(
            () => {
              this.requestsHolder.splice(this.requestsHolder.indexOf(request), 1);
              this.requestsOb.next(this.requestsHolder);
            }
          )
          .catch(response => this.handleError(response));
        })
        .catch(response => this.handleError(response));
  }

  decline(request: any) {
    this.http.put(`${declineRequestUrl + request.id}`, null).toPromise()
        .then(() => {
          Swal.fire(
            'Good job!',
            'You have declined request for paper: ' + request.title,
            'success'
          )
          .then(
            () => {
              this.requestsHolder.splice(this.requestsHolder.indexOf(request), 1);
              this.requestsOb.next(this.requestsHolder);
            }
          )
          .catch(response => this.handleError(response));
        })
        .catch(response => this.handleError(response));
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

  getReviewPapers() {
    this.http.get(`${forReviewUrl}`, {
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
        delete responseInJson.response.papers[`#text`];
        if (!responseInJson.response.papers.paper.length) {
          const holder = [];
          holder.push(responseInJson.response.papers.paper);
          this.requestsHolder = holder;
          this.requestsOb.next(this.requestsHolder);
        } else {
          this.requestsHolder = responseInJson.response.papers.paper;
          this.requestsOb.next(this.requestsHolder);
        }
      }
    )
    .catch(
      response => this.handleError(response)
    );
  }



}
