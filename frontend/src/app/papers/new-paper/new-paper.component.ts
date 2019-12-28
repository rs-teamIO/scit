import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { PaperService } from 'src/app/core/http/paper.service';

@Component({
  selector: 'papers-new-paper',
  templateUrl: './new-paper.component.html',
  styleUrls: ['./new-paper.component.css']
})
export class NewPaperComponent implements OnInit {

  @ViewChild('paperFrame', {static: false}) paperFrame: ElementRef;

  @ViewChild('coverLetterFrame', {static: false}) coverLetterFrame: ElementRef;


  constructor(
    private paperService: PaperService,
  ) { }

  ngOnInit() {
  }

  loadPaper() {
    this.paperFrame.nativeElement.contentWindow.start();
  }

}
