jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LeaveApplicationService } from '../service/leave-application.service';
import { ILeaveApplication, LeaveApplication } from '../leave-application.model';
import { ILeaveType } from 'app/entities/leave-type/leave-type.model';
import { LeaveTypeService } from 'app/entities/leave-type/service/leave-type.service';
import { ILeaveStatus } from 'app/entities/leave-status/leave-status.model';
import { LeaveStatusService } from 'app/entities/leave-status/service/leave-status.service';
import { LeaveApplicationUpdateComponent } from './leave-application-update.component';

describe('Component Tests', () => {
  describe('LeaveApplication Management Update Component', () => {
    let comp: LeaveApplicationUpdateComponent;
    let fixture: ComponentFixture<LeaveApplicationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let leaveApplicationService: LeaveApplicationService;
    let leaveTypeService: LeaveTypeService;
    let leaveStatusService: LeaveStatusService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LeaveApplicationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LeaveApplicationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeaveApplicationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      leaveApplicationService = TestBed.inject(LeaveApplicationService);
      leaveTypeService = TestBed.inject(LeaveTypeService);
      leaveStatusService = TestBed.inject(LeaveStatusService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call LeaveType query and add missing value', () => {
        const leaveApplication: ILeaveApplication = { id: 456 };
        const leaveType: ILeaveType = { id: 37821 };
        leaveApplication.leaveType = leaveType;

        const leaveTypeCollection: ILeaveType[] = [{ id: 66922 }];
        spyOn(leaveTypeService, 'query').and.returnValue(of(new HttpResponse({ body: leaveTypeCollection })));
        const additionalLeaveTypes = [leaveType];
        const expectedCollection: ILeaveType[] = [...additionalLeaveTypes, ...leaveTypeCollection];
        spyOn(leaveTypeService, 'addLeaveTypeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ leaveApplication });
        comp.ngOnInit();

        expect(leaveTypeService.query).toHaveBeenCalled();
        expect(leaveTypeService.addLeaveTypeToCollectionIfMissing).toHaveBeenCalledWith(leaveTypeCollection, ...additionalLeaveTypes);
        expect(comp.leaveTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LeaveStatus query and add missing value', () => {
        const leaveApplication: ILeaveApplication = { id: 456 };
        const leaveStatus: ILeaveStatus = { id: 75408 };
        leaveApplication.leaveStatus = leaveStatus;

        const leaveStatusCollection: ILeaveStatus[] = [{ id: 95723 }];
        spyOn(leaveStatusService, 'query').and.returnValue(of(new HttpResponse({ body: leaveStatusCollection })));
        const additionalLeaveStatuses = [leaveStatus];
        const expectedCollection: ILeaveStatus[] = [...additionalLeaveStatuses, ...leaveStatusCollection];
        spyOn(leaveStatusService, 'addLeaveStatusToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ leaveApplication });
        comp.ngOnInit();

        expect(leaveStatusService.query).toHaveBeenCalled();
        expect(leaveStatusService.addLeaveStatusToCollectionIfMissing).toHaveBeenCalledWith(
          leaveStatusCollection,
          ...additionalLeaveStatuses
        );
        expect(comp.leaveStatusesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const leaveApplication: ILeaveApplication = { id: 456 };
        const leaveType: ILeaveType = { id: 61713 };
        leaveApplication.leaveType = leaveType;
        const leaveStatus: ILeaveStatus = { id: 22090 };
        leaveApplication.leaveStatus = leaveStatus;

        activatedRoute.data = of({ leaveApplication });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(leaveApplication));
        expect(comp.leaveTypesSharedCollection).toContain(leaveType);
        expect(comp.leaveStatusesSharedCollection).toContain(leaveStatus);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leaveApplication = { id: 123 };
        spyOn(leaveApplicationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leaveApplication });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: leaveApplication }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(leaveApplicationService.update).toHaveBeenCalledWith(leaveApplication);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leaveApplication = new LeaveApplication();
        spyOn(leaveApplicationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leaveApplication });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: leaveApplication }));
        saveSubject.complete();

        // THEN
        expect(leaveApplicationService.create).toHaveBeenCalledWith(leaveApplication);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leaveApplication = { id: 123 };
        spyOn(leaveApplicationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leaveApplication });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(leaveApplicationService.update).toHaveBeenCalledWith(leaveApplication);
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

      describe('trackLeaveStatusById', () => {
        it('Should return tracked LeaveStatus primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLeaveStatusById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
