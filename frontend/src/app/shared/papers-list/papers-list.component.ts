import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DocumentResponse } from '../model/document-response';


@Component({
  selector: 'shared-papers-list',
  templateUrl: './papers-list.component.html',
  styleUrls: ['./papers-list.component.css']
})
export class PapersListComponent implements OnInit {


  @Input() state: string;
  @Input() papers: DocumentResponse[] = new Array<DocumentResponse>();

  constructor(private router: Router) { }

  ngOnInit() {
  }




}
