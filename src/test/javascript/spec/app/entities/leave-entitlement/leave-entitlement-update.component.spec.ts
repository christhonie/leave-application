import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LeaveApplicationTestModule } from '../../../test.module';
import { LeaveEntitlementUpdateComponent } from 'app/entities/leave-entitlement/leave-entitlement-update.component';
import { LeaveEntitlementService } from 'app/entities/leave-entitlement/leave-entitlement.service';
import { LeaveEntitlement } from 'app/shared/model/leave-entitlement.model';

describe('Component Tests', () => {
  describe('LeaveEntitlement Management Update Component', () => {
    let comp: LeaveEntitlementUpdateComponent;
    let fixture: ComponentFixture<LeaveEntitlementUpdateComponent>;
    let service: LeaveEntitlementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeaveApplicationTestModule],
        declarations: [LeaveEntitlementUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LeaveEntitlementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeaveEntitlementUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LeaveEntitlementService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LeaveEntitlement(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new LeaveEntitlement();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
