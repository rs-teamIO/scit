import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AppRoutingModule } from '../routes/app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { NavBarComponent } from './nav-bar/nav-bar.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { AuthService } from './http/auth.service';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    NavBarComponent,
    NotFoundComponent
  ],
  exports: [
    AppRoutingModule,
    NavBarComponent,
    NotFoundComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserAnimationsModule,
    CommonModule,
    HttpClientModule
  ],
  providers: [
    AuthService
  ]
})
export class CoreModule { }
