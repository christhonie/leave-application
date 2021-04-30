import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaveApplicationTestModule } from '../../../test.module';
import { DecisionsDetailComponent } from 'app/entities/decisions/decisions-detail.component';
import { Decisions } from 'app/shared/model/decisions.model';

describe('Component Tests', () => {
  describe('Decisions Management Detail Component', () => {
    let comp: DecisionsDetailComponent;
    let fixture: ComponentFixture<DecisionsDetailComponent>;
    const route = ({ data: of({ decisions: new Decisions(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeaveApplicationTestModule],
        declarations: [DecisionsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
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
