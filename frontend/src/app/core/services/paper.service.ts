import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { DocumentResponse } from '../../shared/model/document-response';
import { NgxXml2jsonService } from 'ngx-xml2json';
import { saveAs as importedSaveAs } from 'file-saver';


import Swal from 'sweetalert2';
import { Router } from '@angular/router';


const papersUrl = '/api/v1/papers';
const coverLettersUrl = '/api/v1/cover-letters?paper_id=';

const previewUrl = '/api/v1/paper/transform';

const myPapersUrl = '/api/v1/papers/me';
const paperHtmlUrl = '/api/v1/papers/html?paper_id=http://www.scit.org/papers/';

const downloadXMLURL  = '/api/v1/paper/raw/download?paper_id=http://www.scit.org/papers/';
const downloadPDFLURL = '/api/v1/paper/pdf/download?paper_id=http://www.scit.org/papers/';

const submittedPapersUrl = '/api/v1/papers/submitted';

const assignPaperUrl = '/api/v1/papers/assign?paper_id=http://www.scit.org/papers/';
const useridParam = '&user_id=';

const getXmlByIdUrl = '/api/v1/paper/anonymous?paper_id=http://www.scit.org/papers/';

const publishPaperUrl = '/api/v1/paper/publish?paper_id=';

@Injectable({
  providedIn: 'root'
})
export class PaperService {

    private pdfPreOb = new BehaviorSubject<string>('');
    private pdfPreHolder = '';
    pdfPre = this.pdfPreOb.asObservable();

    private papersOb = new BehaviorSubject<any[]>([]);
    private papersHolder: any[] = [];
    papers = this.papersOb.asObservable();

  constructor(
    protected http: HttpClient,
    private router: Router,
    private parser: NgxXml2jsonService
  ) { }

  redirectToPreview(id: string) {
    const idHolder = id.split('/')
    this.router.navigateByUrl('review/' + idHolder[idHolder.length - 1]);
  }

  getXmlByPaperId(id: string): Observable<string>{

    return this.http.get(`${getXmlByIdUrl + id}`, {
      headers: new HttpHeaders({
        'Content-Type': 'application/xml',
        Accept: '*/*, application/xml, application/json'
      }),
      responseType: 'text'
    });
  }

  findMyPapers() {
    this.http.get(`${myPapersUrl}`, {
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
            this.papersHolder = holder;
            this.papersOb.next(this.papersHolder);
          } else {
            this.papersHolder = responseInJson.response.papers.paper;
            this.papersOb.next(this.papersHolder);
          }
        } else {
          this.papersHolder = [];
          this.papersOb.next(this.papersHolder);
        }

      }
    )
    .catch(
      response => this.handleError(response)
    );
  }

  findSubmittedPapers() {
    this.http.get(`${submittedPapersUrl}`, {
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
            this.papersHolder = holder;
            this.papersOb.next(this.papersHolder);
          } else {
            this.papersHolder = responseInJson.response.papers.paper;
            this.papersOb.next(this.papersHolder);
          }
        } else {
          this.papersHolder = [];
          this.papersOb.next(this.papersHolder);
        }

      }
    )
    .catch(
      response => this.handleError(response)
    );
  }

  preview(xml: string) {
    this.http.post(`${previewUrl}`, this.addPaperInfo(xml), {
      headers: new HttpHeaders({
        'Content-Type': 'application/xml',
        Accept: '*/*, application/xml, application/json'
      }),
      responseType: 'text'
    }).toPromise()
    .then(data => {
      this.pdfPreHolder = data;
      this.pdfPreOb.next(this.pdfPreHolder);
    })
    .catch(
      response => this.handleError(response)
    );
  }

  overview(id: string) {
    console.log(paperHtmlUrl + id);
    this.http.get(`${paperHtmlUrl + id}`, {
      headers: new HttpHeaders({
        'Content-Type': 'application/xml',
        Accept: '*/*, application/xml, application/json'
      }),
      responseType: 'text'
    }).toPromise()
    .then(data => {
      this.pdfPreHolder = data;
      this.pdfPreOb.next(this.pdfPreHolder);
    })
    .catch(
      response => this.handleError(response)
    );
  }

  save(xml: string, coverLetter: string) {

    this.http.post(`${papersUrl}`, this.addPaperInfo(xml), {observe: 'response'}).toPromise()
    .then( response => {
      const id = response.headers.get('Location');
      this.http.post(`${coverLettersUrl + id.split('=')[1]}`, coverLetter, {observe: 'response'}).toPromise()
      .then(
        () => {
          Swal.fire(
            'Good job!',
            'You have submitted your paper.',
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

  processing(paper: any) {
    const idArray = paper.id.split('/');
    this.router.navigateByUrl('papers/' + idArray[idArray.length - 1] + '/processing');
  }

  overviewRedirect(paper: any) {
    const idArray = paper.id.split('/');
    this.router.navigateByUrl('papers/' + idArray[idArray.length - 1]);
  }

  downloadPDF(id: string) {
    this.http.get(`${downloadPDFLURL + id}`, {
      headers: new HttpHeaders({
        Accept: '*/*, application/pdf, application/json'
      }),
      responseType: 'blob'
    }).toPromise()
    .then(blob => importedSaveAs(blob, 'paper.pdf'))
    .catch(
      response => {
        this.handleError(response);
    });
  }

  downloadXML(id: string) {
    this.http.get(`${downloadXMLURL + id}`, {
      headers: new HttpHeaders({
        Accept: '*/*, application/pdf, application/json'
      }),
      responseType: 'blob'
    }).toPromise()
    .then( blob => importedSaveAs(blob, 'paper.xml'))
    .catch(
      response => {
        this.handleError(response);
    });
  }

  assignPaper(chosenAuthors: boolean[], authors: any[], paperId: string) {
    const path = assignPaperUrl + paperId + useridParam;
    for (let i = 0; i < chosenAuthors.length; i++) {
      if (chosenAuthors[i]) {
        this.http.put(`${path + authors[i].id}`, authors[i]).toPromise()
        /*
        .then( () => {
          this.papersHolder.forEach( paper => {
            if (paper.id === 'http://www.scit.org/papers/' + paperId) {
              paper.status = 'in_review';
            }
            this.papersOb.next(this.papersHolder);
          });
        })
        */
        .catch(response => this.handleError(response));
      }
    }
    this.router.navigateByUrl('papers');
  }

  publishPaper(paper: any) {
    this.http.put(`${publishPaperUrl + paper.id}`, null).toPromise()
        .then( response =>
          Swal.fire(
            'Good job!',
            'You have published paper.',
            'success'
          ).then( () => paper.status = 'published')
        )
        .catch(response => this.handleError(response));
  }

}
