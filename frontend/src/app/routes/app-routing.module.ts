import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {NotFoundComponent} from '../core/not-found/not-found.component';
import { SignupComponent } from '../auth/signup/signup.component';
import { SigninComponent } from '../auth/signin/signin.component';
import { NewPaperComponent } from '../papers/new-paper/new-paper.component';
import { AuthorPapersComponent } from '../papers/author-papers/author-papers.component';
import { PaperProcessingComponent } from '../papers/paper-processing/paper-processing.component';
import { PaperDetailsComponent } from '../papers/paper-details/paper-details.component';
import { ReviewsRequestsComponent } from '../reviews/reviews-requests/reviews-requests.component';
import { ReviewNewComponent } from '../reviews/review-new/review-new.component';



const routes: Routes = [
  { path: 'signup', component: SignupComponent },
  { path: 'signin', component: SigninComponent },

  { path: 'new-paper', component: NewPaperComponent},
  { path: 'papers', component: AuthorPapersComponent},
  { path: 'papers/:id/processing', component: PaperProcessingComponent },
  { path: 'papers/:id', component: PaperDetailsComponent },

  { path: 'review/:id', component: ReviewNewComponent },
  { path: 'review-requests', component: ReviewsRequestsComponent },



  { path: '**', component: NotFoundComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
