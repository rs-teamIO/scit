import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReviewNewComponent } from './review-new/review-new.component';
import { ReviewsRequestsComponent } from './reviews-requests/reviews-requests.component';
import { SharedModule } from '../shared/shared.module';
import { EvaluationFormComponent } from './evaluation-form/evaluation-form.component';



@NgModule({
  declarations: [
    ReviewNewComponent,
    ReviewsRequestsComponent,
    EvaluationFormComponent
  ],
  imports: [
    CommonModule,
    SharedModule
  ],
  exports: [
    ReviewNewComponent
  ]
})
export class ReviewsModule { }
