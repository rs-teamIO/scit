import { Component, OnInit } from '@angular/core';
import { ReviewService } from 'src/app/core/services/review.service';

@Component({
  selector: 'app-reviews-requests',
  templateUrl: './reviews-requests.component.html',
  styleUrls: ['./reviews-requests.component.css']
})
export class ReviewsRequestsComponent implements OnInit {

  requests: any[] = [];

  constructor(
    private reviewService: ReviewService
  ) { }

  ngOnInit() {

    this.reviewService.getReviewRequest();
    this.reviewService.requests.subscribe(data => this.requests = data);
  }

  acceptClick(event, request: any) {
    this.reviewService.accept(request);
  }

  declineClick(event, request: any) {
    this.reviewService.decline(request);
  }
}
