import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaveApplicationDetailComponent } from './leave-application-detail.component';

describe('Component Tests', () => {
  describe('LeaveApplication Management Detail Component', () => {
    let comp: LeaveApplicationDetailComponent;
    let fixture: ComponentFixture<LeaveApplicationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LeaveApplicationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ leaveApplication: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LeaveApplicationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LeaveApplicationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load leaveApplication on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.leaveApplication).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
