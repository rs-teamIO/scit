import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';

import { CoreModule } from './core/core.module';
import { AuthModule } from './auth/auth.module';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    AuthModule,
    BrowserModule,
    CoreModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
