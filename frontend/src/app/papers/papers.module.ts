import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NewPaperComponent } from './new-paper/new-paper.component';
import { SharedModule } from '../shared/shared.module';
import { AuthorPapersComponent } from './author-papers/author-papers.component';
import { PaperProcessingComponent } from './paper-processing/paper-processing.component';
import { PaperDetailsComponent } from './paper-details/paper-details.component';
import { FileSaverModule } from 'ngx-filesaver';
import { HomeComponent } from './home/home.component';
import { SearchComponent } from './search/search.component';
import { MetaSearchComponent } from './meta-search/meta-search.component';
import { CoreModule } from '../core/core.module';




@NgModule({
  declarations: [
    NewPaperComponent,
    AuthorPapersComponent,
    PaperProcessingComponent,
    PaperDetailsComponent,
    HomeComponent,
    SearchComponent,
    MetaSearchComponent
  ],
  exports: [
    NewPaperComponent
  ],
  imports: [
    FileSaverModule,
    CommonModule,
    SharedModule,
    CoreModule
  ]
})
export class PapersModule { }
