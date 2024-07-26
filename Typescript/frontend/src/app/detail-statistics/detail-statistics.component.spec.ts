import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailStatisticsComponent } from './detail-statistics.component';

describe('DetailStatisticsComponent', () => {
  let component: DetailStatisticsComponent;
  let fixture: ComponentFixture<DetailStatisticsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DetailStatisticsComponent]
    });
    fixture = TestBed.createComponent(DetailStatisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
