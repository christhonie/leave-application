import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PublicHolidayDetailComponent } from './public-holiday-detail.component';

describe('Component Tests', () => {
  describe('PublicHoliday Management Detail Component', () => {
    let comp: PublicHolidayDetailComponent;
    let fixture: ComponentFixture<PublicHolidayDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PublicHolidayDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ publicHoliday: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PublicHolidayDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PublicHolidayDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load publicHoliday on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.publicHoliday).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
