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
    this.paperService.preview(this.harvestPaper()).subscribe(
      res => {
        this.paperPreview = res;
      },
      err => { });
  }

  save(){
    const paperXml = this.harvestPaper();
    const coverLetterXml = this.harvestCoverLetter(); //UNDONE
    this.paperService.save(paperXml)
    .subscribe(
      response => {
        console.log(response); //add paperId to coverLetterId and change background to process this
      }, 
      err => { }
    );
  }

  harvestPaper(): string {
    let paperXml = this.paperFrame.nativeElement.contentWindow.Xonomy.harvest();
    paperXml = paperXml.replace(/xml:space='preserve'/g, '');
    return paperXml;
  }

  harvestCoverLetter(): string {
    let coverLetterXml = this.coverLetterFrame.nativeElement.contentWindow.Xonomy.harvest();
    coverLetterXml = coverLetterXml.replace(/xml:space='preserve'/g, '');

    return coverLetterXml;
  }


}
