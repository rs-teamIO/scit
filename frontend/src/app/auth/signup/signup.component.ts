import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';
import { SignUpRequest } from 'src/app/shared/model/signup-request';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  firstName: string;
  lastName: string;
  username: string;
  password: string;
  email: string;

  constructor(
    private authService: AuthService
    ) { }

  ngOnInit() {
  }

  signup() {
    this.authService.signup(this.firstName, this.lastName, this.username, this.password, this.email);
  }
}
