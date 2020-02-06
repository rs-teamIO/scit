import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  userForm: FormGroup;

  constructor(
    private authService: AuthService
    ) { }

  ngOnInit() {

    this.userForm = new FormGroup({
      firstName: new FormControl('', [
          Validators.required,
          Validators.maxLength(255)
      ]),
      lastName: new FormControl('', [
        Validators.required,
        Validators.maxLength(255)
      ]),
      username: new FormControl('', [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(255)
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(255)
      ]),
      email: new FormControl('', [
        Validators.required,
        Validators.pattern('[^@]+@[^\.]+\..+')
      ]),
    });

  }

  get firstName() {
    return this.userForm.get('firstName');
  }

  get lastName() {
    return this.userForm.get('lastName');
  }

  get username() {
    return this.userForm.get('username');
  }

  get password() {
    return this.userForm.get('password');
  }

  get email() {
    return this.userForm.get('email');
  }


  signup() {
    const { firstName, lastName, username, password, email} = this.userForm.value;
    this.authService.signup(firstName, lastName, username, password, email);
  }
}
