import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MetaSearchComponent } from './meta-search.component';

describe('MetaSearchComponent', () => {
  let component: MetaSearchComponent;
  let fixture: ComponentFixture<MetaSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MetaSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MetaSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
