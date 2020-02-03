import { Component, OnInit } from '@angular/core';
import { PaperService } from 'src/app/core/services/paper.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-review-new',
  templateUrl: './review-new.component.html',
  styleUrls: ['./review-new.component.css']
})
export class ReviewNewComponent implements OnInit {

  id: string;

  constructor(
    private route: ActivatedRoute,
    private paperService: PaperService
  ) { }

  ngOnInit() {
    this.getId();
    // console.log(this.paperService.getXmlByPaperId(this.id)); TODO
  }


  getId() {
    this.route.params.subscribe(params => {
      this.id = params.id;
    });
  }
}
