import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaveApplicationTestModule } from '../../../test.module';
import { LeaveEntitlementDetailComponent } from 'app/entities/leave-entitlement/leave-entitlement-detail.component';
import { LeaveEntitlement } from 'app/shared/model/leave-entitlement.model';

describe('Component Tests', () => {
  describe('LeaveEntitlement Management Detail Component', () => {
    let comp: LeaveEntitlementDetailComponent;
    let fixture: ComponentFixture<LeaveEntitlementDetailComponent>;
    const route = ({ data: of({ leaveEntitlement: new LeaveEntitlement(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeaveApplicationTestModule],
        declarations: [LeaveEntitlementDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
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
