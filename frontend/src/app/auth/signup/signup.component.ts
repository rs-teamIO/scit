import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { Validators, FormControl, FormGroup } from '@angular/forms';

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
  userForm: FormGroup;

  constructor(
    private authService: AuthService
    ) { }

  ngOnInit() {

    this.userForm = new FormGroup({
      firstName: new FormControl(this.firstName, [
          Validators.required,
          Validators.maxLength(255)
      ]),
      lastName: new FormControl(this.lastName, [
        Validators.required,
        Validators.maxLength(255)
      ]),
      username: new FormControl(this.username, [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(255)
      ]),
      password: new FormControl(this.password, [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(255)
      ]),
      email: new FormControl(this.email, [
        Validators.required,
        Validators.pattern('[^@]+@[^\.]+\..+')
      ]),
    });

  }

  signup() {
    this.authService.signup(this.firstName, this.lastName, this.username, this.password, this.email);
  }
}
