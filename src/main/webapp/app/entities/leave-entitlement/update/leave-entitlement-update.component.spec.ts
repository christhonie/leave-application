jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LeaveEntitlementService } from '../service/leave-entitlement.service';
import { ILeaveEntitlement, LeaveEntitlement } from '../leave-entitlement.model';
import { ILeaveType } from 'app/entities/leave-type/leave-type.model';
import { LeaveTypeService } from 'app/entities/leave-type/service/leave-type.service';
import { IStaff } from 'app/entities/staff/staff.model';
import { StaffService } from 'app/entities/staff/service/staff.service';

import { LeaveEntitlementUpdateComponent } from './leave-entitlement-update.component';

describe('Component Tests', () => {
  describe('LeaveEntitlement Management Update Component', () => {
    let comp: LeaveEntitlementUpdateComponent;
    let fixture: ComponentFixture<LeaveEntitlementUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let leaveEntitlementService: LeaveEntitlementService;
    let leaveTypeService: LeaveTypeService;
    let staffService: StaffService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LeaveEntitlementUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LeaveEntitlementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeaveEntitlementUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      leaveEntitlementService = TestBed.inject(LeaveEntitlementService);
      leaveTypeService = TestBed.inject(LeaveTypeService);
      staffService = TestBed.inject(StaffService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call LeaveType query and add missing value', () => {
        const leaveEntitlement: ILeaveEntitlement = { id: 456 };
        const leaveType: ILeaveType = { id: 12028 };
        leaveEntitlement.leaveType = leaveType;

        const leaveTypeCollection: ILeaveType[] = [{ id: 23696 }];
        spyOn(leaveTypeService, 'query').and.returnValue(of(new HttpResponse({ body: leaveTypeCollection })));
        const additionalLeaveTypes = [leaveType];
        const expectedCollection: ILeaveType[] = [...additionalLeaveTypes, ...leaveTypeCollection];
        spyOn(leaveTypeService, 'addLeaveTypeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ leaveEntitlement });
        comp.ngOnInit();

        expect(leaveTypeService.query).toHaveBeenCalled();
        expect(leaveTypeService.addLeaveTypeToCollectionIfMissing).toHaveBeenCalledWith(leaveTypeCollection, ...additionalLeaveTypes);
        expect(comp.leaveTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Staff query and add missing value', () => {
        const leaveEntitlement: ILeaveEntitlement = { id: 456 };
        const staff: IStaff = { id: 48141 };
        leaveEntitlement.staff = staff;

        const staffCollection: IStaff[] = [{ id: 33566 }];
        spyOn(staffService, 'query').and.returnValue(of(new HttpResponse({ body: staffCollection })));
        const additionalStaff = [staff];
        const expectedCollection: IStaff[] = [...additionalStaff, ...staffCollection];
        spyOn(staffService, 'addStaffToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ leaveEntitlement });
        comp.ngOnInit();

        expect(staffService.query).toHaveBeenCalled();
        expect(staffService.addStaffToCollectionIfMissing).toHaveBeenCalledWith(staffCollection, ...additionalStaff);
        expect(comp.staffSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const leaveEntitlement: ILeaveEntitlement = { id: 456 };
        const leaveType: ILeaveType = { id: 83773 };
        leaveEntitlement.leaveType = leaveType;
        const staff: IStaff = { id: 51416 };
        leaveEntitlement.staff = staff;

        activatedRoute.data = of({ leaveEntitlement });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(leaveEntitlement));
        expect(comp.leaveTypesSharedCollection).toContain(leaveType);
        expect(comp.staffSharedCollection).toContain(staff);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leaveEntitlement = { id: 123 };
        spyOn(leaveEntitlementService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leaveEntitlement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: leaveEntitlement }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(leaveEntitlementService.update).toHaveBeenCalledWith(leaveEntitlement);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leaveEntitlement = new LeaveEntitlement();
        spyOn(leaveEntitlementService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leaveEntitlement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: leaveEntitlement }));
        saveSubject.complete();

        // THEN
        expect(leaveEntitlementService.create).toHaveBeenCalledWith(leaveEntitlement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leaveEntitlement = { id: 123 };
        spyOn(leaveEntitlementService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leaveEntitlement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(leaveEntitlementService.update).toHaveBeenCalledWith(leaveEntitlement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackLeaveTypeById', () => {
        it('Should return tracked LeaveType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLeaveTypeById(0, entity);
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
