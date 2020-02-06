import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { PaperService } from 'src/app/core/services/paper.service';
import { ActivatedRoute } from '@angular/router';
import { ReviewService } from 'src/app/core/services/review.service';
import { EvaluationForm } from 'src/app/shared/model/evaluation-form';

@Component({
  selector: 'app-review-new',
  templateUrl: './review-new.component.html',
  styleUrls: ['./review-new.component.css']
})
export class ReviewNewComponent implements OnInit {

  @ViewChild('paperFrame', {static: false}) paperFrame: ElementRef;

  evaluationForm: EvaluationForm;
  id: string;

  constructor(
    private route: ActivatedRoute,
    private paperService: PaperService,
    private reviewService: ReviewService
  ) { }

  ngOnInit() {
    this.reviewService.setEvaluationForm(
      new EvaluationForm(5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, '')
    );
    this.reviewService.evaluationForm.subscribe( data => this.evaluationForm = data );
  }


  loadPaper() {
    this.route.params.subscribe(params => {
      this.id = params.id;
      this.paperService.getXmlByPaperId(params.id).subscribe( data =>
        this.paperFrame.nativeElement.contentWindow.start(localStorage.getItem('username'), data) );
    });
  }

  harvestReview(): string {
    let reviewXml = this.paperFrame.nativeElement.contentWindow.Xonomy.harvest();
    reviewXml = reviewXml.replace(/xml:space='preserve'/g, '');
    return reviewXml;
  }

  confirmClick() {
    const xml = this.harvestReview();
    this.reviewService.save(xml, this.evaluationForm.getXmlEvaluatinForm(), this.id);
  }

}
