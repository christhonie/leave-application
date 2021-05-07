import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaveApplicationTestModule } from '../../../test.module';
import { LeaveStatusDetailComponent } from 'app/entities/leave-status/leave-status-detail.component';
import { LeaveStatus } from 'app/shared/model/leave-status.model';

describe('Component Tests', () => {
  describe('LeaveStatus Management Detail Component', () => {
    let comp: LeaveStatusDetailComponent;
    let fixture: ComponentFixture<LeaveStatusDetailComponent>;
    const route = ({ data: of({ leaveStatus: new LeaveStatus(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeaveApplicationTestModule],
        declarations: [LeaveStatusDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
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
