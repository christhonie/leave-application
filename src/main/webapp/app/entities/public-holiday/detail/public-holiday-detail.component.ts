import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPublicHoliday } from '../public-holiday.model';

@Component({
  selector: 'jhi-public-holiday-detail',
  templateUrl: './public-holiday-detail.component.html',
})
export class PublicHolidayDetailComponent implements OnInit {
  publicHoliday: IPublicHoliday | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ publicHoliday }) => {
      this.publicHoliday = publicHoliday;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
