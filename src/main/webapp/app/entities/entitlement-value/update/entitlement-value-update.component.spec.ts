jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EntitlementValueService } from '../service/entitlement-value.service';
import { IEntitlementValue, EntitlementValue } from '../entitlement-value.model';
import { ILeaveEntitlement } from 'app/entities/leave-entitlement/leave-entitlement.model';
import { LeaveEntitlementService } from 'app/entities/leave-entitlement/service/leave-entitlement.service';
import { IStaff } from 'app/entities/staff/staff.model';
import { StaffService } from 'app/entities/staff/service/staff.service';

import { EntitlementValueUpdateComponent } from './entitlement-value-update.component';

describe('Component Tests', () => {
  describe('EntitlementValue Management Update Component', () => {
    let comp: EntitlementValueUpdateComponent;
    let fixture: ComponentFixture<EntitlementValueUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let entitlementValueService: EntitlementValueService;
    let leaveEntitlementService: LeaveEntitlementService;
    let staffService: StaffService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EntitlementValueUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EntitlementValueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EntitlementValueUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      entitlementValueService = TestBed.inject(EntitlementValueService);
      leaveEntitlementService = TestBed.inject(LeaveEntitlementService);
      staffService = TestBed.inject(StaffService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call entitlement query and add missing value', () => {
        const entitlementValue: IEntitlementValue = { id: 456 };
        const entitlement: ILeaveEntitlement = { id: 77694 };
        entitlementValue.entitlement = entitlement;

        const entitlementCollection: ILeaveEntitlement[] = [{ id: 61052 }];
        spyOn(leaveEntitlementService, 'query').and.returnValue(of(new HttpResponse({ body: entitlementCollection })));
        const expectedCollection: ILeaveEntitlement[] = [entitlement, ...entitlementCollection];
        spyOn(leaveEntitlementService, 'addLeaveEntitlementToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ entitlementValue });
        comp.ngOnInit();

        expect(leaveEntitlementService.query).toHaveBeenCalled();
        expect(leaveEntitlementService.addLeaveEntitlementToCollectionIfMissing).toHaveBeenCalledWith(entitlementCollection, entitlement);
        expect(comp.entitlementsCollection).toEqual(expectedCollection);
      });

      it('Should call staff query and add missing value', () => {
        const entitlementValue: IEntitlementValue = { id: 456 };
        const staff: IStaff = { id: 20969 };
        entitlementValue.staff = staff;

        const staffCollection: IStaff[] = [{ id: 84658 }];
        spyOn(staffService, 'query').and.returnValue(of(new HttpResponse({ body: staffCollection })));
        const expectedCollection: IStaff[] = [staff, ...staffCollection];
        spyOn(staffService, 'addStaffToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ entitlementValue });
        comp.ngOnInit();

        expect(staffService.query).toHaveBeenCalled();
        expect(staffService.addStaffToCollectionIfMissing).toHaveBeenCalledWith(staffCollection, staff);
        expect(comp.staffCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const entitlementValue: IEntitlementValue = { id: 456 };
        const entitlement: ILeaveEntitlement = { id: 99529 };
        entitlementValue.entitlement = entitlement;
        const staff: IStaff = { id: 94299 };
        entitlementValue.staff = staff;

        activatedRoute.data = of({ entitlementValue });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(entitlementValue));
        expect(comp.entitlementsCollection).toContain(entitlement);
        expect(comp.staffCollection).toContain(staff);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const entitlementValue = { id: 123 };
        spyOn(entitlementValueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ entitlementValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: entitlementValue }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(entitlementValueService.update).toHaveBeenCalledWith(entitlementValue);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const entitlementValue = new EntitlementValue();
        spyOn(entitlementValueService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ entitlementValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: entitlementValue }));
        saveSubject.complete();

        // THEN
        expect(entitlementValueService.create).toHaveBeenCalledWith(entitlementValue);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const entitlementValue = { id: 123 };
        spyOn(entitlementValueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ entitlementValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(entitlementValueService.update).toHaveBeenCalledWith(entitlementValue);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackLeaveEntitlementById', () => {
        it('Should return tracked LeaveEntitlement primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLeaveEntitlementById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackStaffById', () => {
        it('Should return tracked Staff primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackStaffById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
