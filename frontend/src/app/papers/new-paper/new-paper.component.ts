import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { PaperService } from 'src/app/core/services/paper.service';

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
    this.paperService.pdfPre.subscribe(
      data => this.paperPreview = data
    );
  }

  loadPaper() {
    this.paperFrame.nativeElement.contentWindow.start();
  }

  getPreview() {
    // this.paperService.preview(this.harvestPaper()).subscribe(
    //   res => {
    //     this.paperPreview = res;
    //   },
    //   err => { });.
    const paperXml = this.harvestPaper();
    this.paperService.preview(paperXml);  // OVDE SI STAO
    console.log(`KLIKNUO`);
  }

  save() {
    const paperXml = this.harvestPaper();
    const coverLetterXml = this.harvestCoverLetter();
    // console.log(this.paperService.addPaperInfo(paperXml));
    this.paperService.save(paperXml, coverLetterXml);
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
