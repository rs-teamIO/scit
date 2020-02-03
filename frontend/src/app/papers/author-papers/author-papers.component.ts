import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { DocumentResponse } from '../../shared/model/document-response';
import { PaperService } from '../../core/services/paper.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { ReviewService } from 'src/app/core/services/review.service';


@Component({
  selector: 'app-author-papers',
  templateUrl: './author-papers.component.html',
  styleUrls: ['./author-papers.component.css']
})
export class AuthorPapersComponent implements OnInit {

  username: string;
  papers: DocumentResponse[];
  noSubmittedPapers: string;
  author: boolean;
  title: string;

  reviews: any[];


  constructor(
    private authService: AuthService,
    private paperService: PaperService,
    private reviewService: ReviewService,

    private route: ActivatedRoute
    ) { }

  ngOnInit() {
    this.author = this.authService.isAuthor();
    if (this.author) {
      this.title = 'Papers!';
      this.username = localStorage.getItem('username');
      this.paperService.findMyPapers();
      this.reviewService.getReviewPapers();
      this.reviewService.requests.subscribe(data => this.reviews = data);
    } else {
      this.title = 'Submitted Papers!';
      this.paperService.findSubmittedPapers();
    }
    this.paperService.papers.subscribe( data => this.papers = data);
  }
}


