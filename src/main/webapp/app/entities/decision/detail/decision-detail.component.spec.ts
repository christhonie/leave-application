import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DecisionDetailComponent } from './decision-detail.component';

describe('Component Tests', () => {
  describe('Decision Management Detail Component', () => {
    let comp: DecisionDetailComponent;
    let fixture: ComponentFixture<DecisionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DecisionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ decision: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DecisionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DecisionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load decision on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.decision).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
