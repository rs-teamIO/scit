import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NewPaperComponent } from './new-paper/new-paper.component';
import { SharedModule } from '../shared/shared.module';



@NgModule({
  declarations: [
    NewPaperComponent
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
