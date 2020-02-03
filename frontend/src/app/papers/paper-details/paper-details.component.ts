import { Component, OnInit } from '@angular/core';
import { PaperService } from 'src/app/core/services/paper.service';
import { Router, ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-paper-details',
  templateUrl: './paper-details.component.html',
  styleUrls: ['./paper-details.component.css']
})
export class PaperDetailsComponent implements OnInit {

  paperOverview: string;
  id: string;


  constructor(
    private route: ActivatedRoute,
    private paperService: PaperService
    ) { }

  ngOnInit() {
    this.getId();
    this.paperService.overview(this.id);
    this.paperService.pdfPre.subscribe( data => this.paperOverview = data);
  }

  getId() {
    this.route.params.subscribe(params => {
      this.id = params.id;
    });
  }

  downloadPDF() {
    this.paperService.downloadPDF(this.id);
  }

  downloadXML() {
    this.paperService.downloadXML(this.id);
  }


}
