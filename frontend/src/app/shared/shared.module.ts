import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CollapseModule } from 'ngx-bootstrap/collapse';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ModalModule } from 'ngx-bootstrap/modal';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { PapersListComponent } from './papers-list/papers-list.component';
import { SafeHtmlPipe } from './pipe/safe-html.pipe';


@NgModule({
  imports: [
    BsDropdownModule.forRoot(),
    CollapseModule.forRoot(),
    TabsModule.forRoot(),
    CommonModule,
    ModalModule.forRoot(),
    FormsModule
  ],
  exports: [
    BsDropdownModule,
    CollapseModule,
    TabsModule,
    FormsModule,
    SafeHtmlPipe,
    PapersListComponent
  ],
  declarations: [PapersListComponent, SafeHtmlPipe]
})
export class SharedModule { }
