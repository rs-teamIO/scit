import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service';
import { PaperService } from 'src/app/core/services/paper.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-paper-processing',
  templateUrl: './paper-processing.component.html',
  styleUrls: ['./paper-processing.component.css']
})
export class PaperProcessingComponent implements OnInit {

  authors: any[] = [];
  checkBoxStates: boolean[] = [];
  id: string;


  constructor(
    private userService: UserService,
    private paperService: PaperService,
    private route: ActivatedRoute,
  ) { }

  ngOnInit() {
    this.getId();
    this.userService.getAllAuthors();

    this.userService.authors.subscribe(
      data => {
        // tslint:disable-next-line: prefer-for-of
        for (let i = 0; i < data.length; i++) {
          this.checkBoxStates.push(false);
        }
        this.authors = data;
      }
    );
  }

  getId() {
    this.route.params.subscribe(params => {
      this.id = params.id;
    });
  }

  assignClick() {
    this.paperService.assignPaper(this.checkBoxStates, this.authors, this.id);
  }


}

