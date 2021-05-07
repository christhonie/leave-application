import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaveEntitlementDetailComponent } from './leave-entitlement-detail.component';

describe('Component Tests', () => {
  describe('LeaveEntitlement Management Detail Component', () => {
    let comp: LeaveEntitlementDetailComponent;
    let fixture: ComponentFixture<LeaveEntitlementDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LeaveEntitlementDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ leaveEntitlement: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LeaveEntitlementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LeaveEntitlementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load leaveEntitlement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.leaveEntitlement).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
