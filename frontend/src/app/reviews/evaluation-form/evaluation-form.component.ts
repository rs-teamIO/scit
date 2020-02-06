import { Component, OnInit } from '@angular/core';
import { ReviewService } from 'src/app/core/services/review.service';
import { EvaluationForm } from 'src/app/shared/model/evaluation-form';

@Component({
  selector: 'app-evaluation-form',
  templateUrl: './evaluation-form.component.html',
  styleUrls: ['./evaluation-form.component.css']
})
export class EvaluationFormComponent implements OnInit {

  evaluationForm: EvaluationForm;

  constructor(
    private reviewService: ReviewService
  ) {

    this.evaluationForm = new EvaluationForm(5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, '');

  }

  ngOnInit() {
    this.reviewService.evaluationForm.subscribe( data => this.evaluationForm = data );
  }

}
