import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {NotFoundComponent} from '../core/not-found/not-found.component';
import { SignupComponent } from '../auth/signup/signup.component';
import { SigninComponent } from '../auth/signin/signin.component';
import { NewPaperComponent } from '../papers/new-paper/new-paper.component';
import { AuthorPapersComponent } from '../papers/author-papers/author-papers.component';



const routes: Routes = [
  { path: 'signup', component: SignupComponent },
  { path: 'signin', component: SigninComponent },

  { path: 'new-paper', component: NewPaperComponent},
  { path: 'papers', component: AuthorPapersComponent},

  { path: '**', component: NotFoundComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
