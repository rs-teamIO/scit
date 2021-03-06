import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SignupComponent } from './signup/signup.component';
import { SigninComponent } from './signin/signin.component';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';





@NgModule({
  declarations: [
    SignupComponent,
    SigninComponent
  ],
  exports: [
    SignupComponent,
    SigninComponent
  ],
  imports: [
    ReactiveFormsModule,
    CommonModule,
    FormsModule
  ]
})
export class AuthModule { }
