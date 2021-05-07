import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaveApplicationTestModule } from '../../../test.module';
import { EntitlementValueDetailComponent } from 'app/entities/entitlement-value/entitlement-value-detail.component';
import { EntitlementValue } from 'app/shared/model/entitlement-value.model';

describe('Component Tests', () => {
  describe('EntitlementValue Management Detail Component', () => {
    let comp: EntitlementValueDetailComponent;
    let fixture: ComponentFixture<EntitlementValueDetailComponent>;
    const route = ({ data: of({ entitlementValue: new EntitlementValue(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeaveApplicationTestModule],
        declarations: [EntitlementValueDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(EntitlementValueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EntitlementValueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load entitlementValue on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.entitlementValue).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
