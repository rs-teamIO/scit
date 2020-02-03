import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewsRequestsComponent } from './reviews-requests.component';

describe('ReviewsRequestsComponent', () => {
  let component: ReviewsRequestsComponent;
  let fixture: ComponentFixture<ReviewsRequestsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewsRequestsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewsRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
