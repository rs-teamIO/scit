import { Component, OnInit } from '@angular/core';
import { AuthService } from '../http/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'core-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
  }

  isAuthenticated() {
    return this.authService.isAuthenticated();
  }

  
  signout() {
    this.authService.signout().subscribe(
      res => {
        this.router.navigateByUrl('signin');
      }, err => { });
  }
}
