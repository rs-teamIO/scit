import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { NgxXml2jsonService } from 'ngx-xml2json';
import Swal from 'sweetalert2';
import { EvaluationForm } from 'src/app/shared/model/evaluation-form';

const myReviewRequestsUrl = '/api/v1/papers/assigned';
const acceptRequestUrl = '/api/v1/review/accept?paper_id=';
const declineRequestUrl = '/api/v1/review/decline?paper_id=';
const forReviewUrl = '/api/v1/papers/in-review';
const reviewUrl = '/api/v1/reviews';
const evaluationFomrUrl = '/api/v1/evaluation-forms?paper_id=http://www.scit.org/papers/';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {


  private requestsOb = new BehaviorSubject<any[]>([]);
  private requestsHolder: any[] = [];
  requests = this.requestsOb.asObservable();


  private evaluationFormOb = new BehaviorSubject<EvaluationForm>(
    new EvaluationForm(5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, '')
    );
  private evaluationFormHolder: EvaluationForm;
  evaluationForm = this.evaluationFormOb.asObservable();



  constructor(
    protected http: HttpClient,
    private router: Router,
    private parser: NgxXml2jsonService
  ) { }

  setEvaluationForm(evaluationForm: EvaluationForm) {
    this.evaluationFormHolder = evaluationForm;
    this.evaluationFormOb.next(this.evaluationFormHolder);
  }

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
        if (responseInJson.response.papers.paper) {
          if (!responseInJson.response.papers.paper.length) {
            const holder = [];
            holder.push(responseInJson.response.papers.paper);
            this.requestsHolder = holder;
            this.requestsOb.next(this.requestsHolder);
          } else {
            this.requestsHolder = responseInJson.response.papers.paper;
            this.requestsOb.next(this.requestsHolder);
          }
        } else {
          this.requestsHolder = [];
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
        if (responseInJson.response.papers.paper) {
          if (!responseInJson.response.papers.paper.length) {
            const holder = [];
            holder.push(responseInJson.response.papers.paper);
            this.requestsHolder = holder;
            this.requestsOb.next(this.requestsHolder);
          } else {
            this.requestsHolder = responseInJson.response.papers.paper;
            this.requestsOb.next(this.requestsHolder);
          }
        } else {
          this.requestsHolder = [];
          this.requestsOb.next(this.requestsHolder);
        }
      }
    )
    .catch(
      response => this.handleError(response)
    );
  }

  save( xml: string, evaluationForm: string, id: string ) {
    this.http.post(`${reviewUrl}`, this.addComment(xml), {observe: 'response'}).toPromise()
    .then( response => {
      this.http.post(`${evaluationFomrUrl + id}`, evaluationForm, {observe: 'response'}).toPromise()
      .then(
        () => {
          Swal.fire(
            'Good job!',
            'You have submitted your review.',
            'success'
          )
          .then(
            () => this.router.navigateByUrl('papers') // save cover_letter
          )
          .catch(
            response => this.handleError(response)
          );
      });
    })
    .catch( response => {
      this.handleError(response);
    });
  }

  addComment(xml: string) {
    const today = new Date();
    const dd = String(today.getDate()).padStart(2, '0');
    const mm = String(today.getMonth() + 1).padStart(2, '0');
    const yyyy = today.getFullYear();

    const toCountLett = `</paper:paper>`;
    const toInsert = `<paper:comment>First paper comment</paper:comment>`;

    return this.insert(xml, toInsert, xml.length - toCountLett.length);

  }

  insert(main: string, ins: string, pos: number) {
    return main.slice(0, pos) + ins + main.slice(pos);
  }


}
