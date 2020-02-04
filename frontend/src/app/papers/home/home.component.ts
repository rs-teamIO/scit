import { Component, OnInit } from '@angular/core';




@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  showSpinner = false;

  typingTimer;                // timer identifier
  doneTypingInterval = 500;


  constructor() { }

  ngOnInit() {
  }

  onKeydown() {
    this.showSpinner = true;
    clearTimeout(this.typingTimer);
  }

  onKeyup() {
    clearTimeout(this.typingTimer);
    this.typingTimer = setTimeout(this.doneTyping, this.doneTypingInterval);
  }

  doneTyping() {
    console.log(this);
    this.showSpinner = false; // bind showSpinner from service and in service...
  }


}
