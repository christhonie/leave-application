import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DecisionsDetailComponent } from './decisions-detail.component';

describe('Component Tests', () => {
  describe('Decisions Management Detail Component', () => {
    let comp: DecisionsDetailComponent;
    let fixture: ComponentFixture<DecisionsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DecisionsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ decisions: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DecisionsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DecisionsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load decisions on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.decisions).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
