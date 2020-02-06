import { Component, OnInit } from '@angular/core';
import { PaperService } from 'src/app/core/services/paper.service';

@Component({
  selector: 'app-meta-search',
  templateUrl: './meta-search.component.html',
  styleUrls: ['./meta-search.component.css']
})
export class MetaSearchComponent implements OnInit {

  showSpinner = false;
  papers: any[];

  query: string;

  doi: string;
  journalId: string;
  category: string;
  yearOfPublishing: number;
  authorName: string;


  constructor(
    private paperService: PaperService
  ) { }

  ngOnInit() {
    this.paperService.spinner.subscribe( data => this.showSpinner = data);
    this.paperService.searchPapers.subscribe( data => this.papers = data);
  }

  searchClick() {
    this.showSpinner = true;
    this.paperService.metadataSearch( this.doi, this.journalId, this.category, this.yearOfPublishing, this.authorName);
  }
}
