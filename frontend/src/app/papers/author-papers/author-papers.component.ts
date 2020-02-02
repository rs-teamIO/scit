import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { DocumentResponse } from '../../shared/model/document-response';
import { PaperService } from '../../core/services/paper.service';


@Component({
  selector: 'app-author-papers',
  templateUrl: './author-papers.component.html',
  styleUrls: ['./author-papers.component.css']
})
export class AuthorPapersComponent implements OnInit {

  username: string;

  papers: DocumentResponse[];

  noSubmittedPapers: string;

  constructor(
    private paperService: PaperService,
    private route: ActivatedRoute
    ) { }

  ngOnInit() {
    this.username = JSON.parse(localStorage.getItem("authenticatedUser")).username;
    this.getPapers();
  }

  getPapers() {
    // this.paperService.findMyPapers().subscribe(
    //   res => {
    //     this.noSubmittedPapers = '';
    //     this.papers = res;

    //     if (this.papers.length === 0) {
    //       this.noSubmittedPapers = 'This author still hasn\'t published any papers.';
    //     }
    //   }, err => { });
  }



}


