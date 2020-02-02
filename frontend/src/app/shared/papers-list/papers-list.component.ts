import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DocumentResponse } from '../model/document-response';
import { PaperService } from 'src/app/core/services/paper.service';


@Component({
  selector: 'shared-papers-list',
  templateUrl: './papers-list.component.html',
  styleUrls: ['./papers-list.component.css']
})
export class PapersListComponent implements OnInit {


  @Input() state: string;
  @Input() papers: [] = [];

  constructor(
    private paperService: PaperService
    ) { }

  ngOnInit() {
  }

  processingClick($event, paper: any) {
    this.paperService.processing(paper);
  }

  titleClick($event, paper: any){
    this.paperService.overviewRedirect(paper);
  }

}
