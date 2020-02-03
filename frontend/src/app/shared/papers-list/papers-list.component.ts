import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DocumentResponse } from '../model/document-response';
import { PaperService } from 'src/app/core/services/paper.service';
import { AuthService } from 'src/app/core/services/auth.service';


@Component({
  selector: 'shared-papers-list',
  templateUrl: './papers-list.component.html',
  styleUrls: ['./papers-list.component.css']
})
export class PapersListComponent implements OnInit {

  @Input() papers: [] = [];
  @Input() type: string;


  constructor(
    private paperService: PaperService,
    private authService: AuthService
    ) { }

  ngOnInit() {
  }

  processingClick($event, paper: any) {
    this.paperService.processing(paper);
  }

  titleClick($event, paper: any){
    this.paperService.overviewRedirect(paper);
  }

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  isAuthor(): boolean {
    return this.authService.isAuthor();
  }

  isEditor(): boolean {
    return this.authService.isEditor();
  }
  reviewPaper(event, paper: any) {
    //console.log(paper);
    this.paperService.redirectToPreview(paper.id);
  }


}
