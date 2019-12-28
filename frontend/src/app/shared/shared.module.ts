import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CollapseModule } from 'ngx-bootstrap/collapse';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ModalModule } from 'ngx-bootstrap/modal';
import { TabsModule } from 'ngx-bootstrap/tabs';


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
  ],
  declarations: []
})
export class SharedModule { }
