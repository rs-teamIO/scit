import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NewPaperComponent } from './new-paper/new-paper.component';
import { SharedModule } from '../shared/shared.module';
import { AuthorPapersComponent } from './author-papers/author-papers.component';



@NgModule({
  declarations: [
    NewPaperComponent,
    AuthorPapersComponent
  ],
  exports: [
    NewPaperComponent
  ],
  imports: [
    CommonModule,
    SharedModule
  ]
})
export class PapersModule { }
