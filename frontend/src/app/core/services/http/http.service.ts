import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(protected http: HttpClient) {

  }

  getToken() {
    return `Bearer `; // TODO
  }

  createHeaders() {
    const headers = new HttpHeaders({
      'Content-Type': 'application/xml',
      'Authorization': `Bearer `
    });
    return {headers};
  }

  get(url: string) {
    return this.http.get(url, this.createHeaders());
  }

  post(url: string, data: Document) {
    return this.http.post(url, data, this.createHeaders());
  }

  put(url: string, data: Document) {
    return this.http.put(url, data, this.createHeaders());
  }

  delete(url: string) {
    return this.http.delete(url, this.createHeaders());
  }

  handleError(response: any) {
    console.error(response); // TODO: remove
    if (response.error) {
      console.log(response.error); // TODO: add sweetalert
    } else {
      console.log('Client side error!'); // TODO: add sweetalert
    }
  }

  handleSuccess(response: any) {
    console.log(response.data); // TODO sweetalert
  }
}
