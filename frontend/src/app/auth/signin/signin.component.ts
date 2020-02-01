import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../../core/services/auth.service';
import { SignInRequest } from 'src/app/shared/model/signin-request';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit {

  username: string;
  password: string;
  constructor(
    private authService: AuthService) { }

  ngOnInit() {
  }

  signin() {
    this.authService.signin(this.username, this.password);
  }

}
