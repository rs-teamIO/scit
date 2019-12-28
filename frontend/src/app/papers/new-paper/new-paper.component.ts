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

  paperPreview: string;


  constructor(
    private paperService: PaperService,
  ) { }

  ngOnInit() {
  }

  loadPaper() {
    this.paperFrame.nativeElement.contentWindow.start();
  }

  getPreview() {
    console.log("TRIGGERED");

    this.paperService.preview(this.harvestPaper()).subscribe(
      res => {
        this.paperPreview = res;
      },
      err => { });
  }

  harvestPaper(): string {
    let paperXml = this.paperFrame.nativeElement.contentWindow.Xonomy.harvest();
    paperXml = paperXml.replace(/xml:space='preserve'/g, '');
    return paperXml;
  }
}
