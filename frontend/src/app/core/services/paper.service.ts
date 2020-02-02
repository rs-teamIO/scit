import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { DocumentResponse } from '../../shared/model/document-response';

import Swal from 'sweetalert2';
import { Router } from '@angular/router';


const papersUrl = '/api/v1/papers';
const previewUrl = '/api/v1/papers/preview';



@Injectable({
  providedIn: 'root'
})
export class PaperService {

    private pdfPreOb = new BehaviorSubject<string>('');
    private pdfPreHolder = '';
    pdfPre = this.pdfPreOb.asObservable();


  constructor(
    protected http: HttpClient,
    private router: Router

  ) { }

  // preview(xml: string): Observable<string> {
  //   return this.http.post(`${url}/preview`, xml, {
  //     headers: new HttpHeaders({
  //       'Content-Type': 'application/xml',
  //       'Accept': '*/*, application/xml, application/json'
  //     }),
  //     responseType: 'text'
  //   }).pipe(
  //     catchError(this.handleError<string>())
  //   );
  // }


  preview(xml: string) {
    this.http.post<string>(`${previewUrl}`, xml).toPromise()
    .then(data => {
      this.pdfPreHolder = data;
      this.pdfPreOb.next(this.pdfPreHolder);
    })
    .catch();
    console.log(`KLIKNUO`);
  }

  save(xml: string, coverLetter: string) {

    this.http.post(`${papersUrl}`, this.addPaperInfo(xml), {observe: 'response'}).toPromise()
    .then( response => {
      console.log(response.headers.get('Location'));
      Swal.fire(
        'Good job!',
        'You have submitted your paper.',
        'success'
      )
      .then(
        () => this.router.navigateByUrl('papers') // save cover_letter
      );
    })
    .catch( response => {
      this.handleError(response);
    });
  }


  findMyPapers() {
    // return this.http.get<DocumentResponse[]>(`${papers}`).pipe(
    //   catchError(this.handleError<DocumentResponse[]>())
    // );
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

  addPaperInfo(xml: string) {
    const today = new Date();
    const dd = String(today.getDate()).padStart(2, '0');
    const mm = String(today.getMonth() + 1).padStart(2, '0');
    const yyyy = today.getFullYear();

    const toCountLett = `<paper:paper xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:paper="http://www.scit.org/schema/paper">`;
    const toInsert = `<paper:paper_info>` +
                    `<paper:journal_id></paper:journal_id>` +
                    `<paper:category></paper:category>` +
                    `<paper:status>submitted</paper:status>` +
                    `<paper:submission_dates>${yyyy + '-' + mm + '-' + dd }</paper:submission_dates>` +
                    `<paper:revision_dates></paper:revision_dates>` +
                    `<paper:acceptance_dates></paper:acceptance_dates>` +
                `</paper:paper_info>`;

    return this.insert(xml, toInsert, toCountLett.length);

  }

  insert(main: string, ins: string, pos: number) {
    return main.slice(0, pos) + ins + main.slice(pos);
  }

}
