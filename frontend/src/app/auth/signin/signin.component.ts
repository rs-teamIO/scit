import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../../core/services/auth.service';
import { SignInRequest } from 'src/app/shared/model/signin-request';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit {

  signInForm: FormGroup;

  constructor(
    private authService: AuthService) { }

  ngOnInit() {
    this.signInForm = new FormGroup({
      username: new FormControl('', [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(255)
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(255)
      ])
    });
  }

  get username() {
    return this.signInForm.get('username');
  }

  get password() {
    return this.signInForm.get('password');
  }

  signin() {
    const { username, password } = this.signInForm.value;
    this.authService.signin(username, password);
  }

}
