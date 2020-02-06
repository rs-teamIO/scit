import { Component, OnInit, Input } from '@angular/core';
import { PaperService } from 'src/app/core/services/paper.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  showSpinner = false;
  papers: any[];

  query: string;

  typingTimer;                // timer identifier
  doneTypingInterval = 500;


  @Input() type: string;

  constructor(
    private paperService: PaperService
  ) { }

  ngOnInit() {
    this.paperService.spinner.subscribe( data => this.showSpinner = data);
    if(this.type === 'author') {
      this.paperService.papers.subscribe( data => this.papers = data);
    } else {
      this.paperService.searchPapers.subscribe( data => this.papers = data);
    }
  }

  onKeydown() {
    this.showSpinner = true;
    clearTimeout(this.typingTimer);
  }

  onKeyup() {
    clearTimeout(this.typingTimer);
    this.typingTimer = setTimeout(() => this.paperService.searchResolver(this.query, this.type), this.doneTypingInterval);
  }

}
