import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/http/auth.service';
import { Router } from '@angular/router';
import { SignUpRequest } from 'src/app/shared/model/signup-request';

@Component({
  selector: 'auth-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {


  username: string;
  password: string;
  email: string;

  constructor(
    private authService: AuthService,
    private router: Router,) { }

  ngOnInit() {
  }

  signup() {
    // tslint:disable-next-line:max-line-length
    //const signupXml = `<?xml version="1.0" encoding="UTF-8"?><user xmlns="http://ftn.uns.ac.rs/code10/user"><username>${this.username}</username><email>${this.email}</email><password>${this.password}</password></user>`;
    this.authService.signup(new SignUpRequest(this.username, this.password, this.email)).subscribe(
      res => {
        console.log('Successful sign up!');
        this.router.navigateByUrl('signin');
      },
      err => { }
    );
  }
}
