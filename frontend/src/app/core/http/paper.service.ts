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
}
