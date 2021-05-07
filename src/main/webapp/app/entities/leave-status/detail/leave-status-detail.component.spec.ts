import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaveStatusDetailComponent } from './leave-status-detail.component';

describe('Component Tests', () => {
  describe('LeaveStatus Management Detail Component', () => {
    let comp: LeaveStatusDetailComponent;
    let fixture: ComponentFixture<LeaveStatusDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LeaveStatusDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ leaveStatus: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LeaveStatusDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LeaveStatusDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load leaveStatus on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.leaveStatus).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
