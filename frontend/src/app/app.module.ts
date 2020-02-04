import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';

import { CoreModule } from './core/core.module';
import { AuthModule } from './auth/auth.module';
import { PapersModule } from './papers/papers.module';

import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpRequestInterceptor } from './core/services/http/http-request.interceptor';
import { CookieService } from 'ngx-cookie-service';
import { NgxXml2jsonService } from 'ngx-xml2json';
import { AuthService } from './core/services/auth.service';
import { UserService } from './core/services/user.service';
import { ReviewService } from './core/services/review.service';
import { ReviewsModule } from './reviews/reviews.module';





@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    AuthModule,
    BrowserModule,
    CoreModule,
    PapersModule,
    ReviewsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptor,
      multi: true
    },
    CookieService,
    NgxXml2jsonService,
    AuthService,
    UserService,
    ReviewService

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
