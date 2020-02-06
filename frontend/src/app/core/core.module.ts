import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AppRoutingModule } from '../routes/app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { NavBarComponent } from './nav-bar/nav-bar.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { AuthService } from './services/auth.service';
import { HttpClientModule } from '@angular/common/http';
import { PaperService } from './services/paper.service';
import { SharedModule } from '../shared/shared.module';
import { HttpService } from './services/http/http.service';

@NgModule({
  declarations: [
    NavBarComponent,
    NotFoundComponent,
  ],
  exports: [
    AppRoutingModule,
    NavBarComponent,
    NotFoundComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserAnimationsModule,
    CommonModule,
    SharedModule,
    HttpClientModule
  ],
  providers: [
    PaperService,
    HttpService
  ]
})
export class CoreModule { }
