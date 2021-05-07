jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LeaveStatusService } from '../service/leave-status.service';
import { ILeaveStatus, LeaveStatus } from '../leave-status.model';

import { LeaveStatusUpdateComponent } from './leave-status-update.component';

describe('Component Tests', () => {
  describe('LeaveStatus Management Update Component', () => {
    let comp: LeaveStatusUpdateComponent;
    let fixture: ComponentFixture<LeaveStatusUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let leaveStatusService: LeaveStatusService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LeaveStatusUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LeaveStatusUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeaveStatusUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      leaveStatusService = TestBed.inject(LeaveStatusService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const leaveStatus: ILeaveStatus = { id: 456 };

        activatedRoute.data = of({ leaveStatus });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(leaveStatus));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leaveStatus = { id: 123 };
        spyOn(leaveStatusService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leaveStatus });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: leaveStatus }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(leaveStatusService.update).toHaveBeenCalledWith(leaveStatus);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leaveStatus = new LeaveStatus();
        spyOn(leaveStatusService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leaveStatus });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: leaveStatus }));
        saveSubject.complete();

        // THEN
        expect(leaveStatusService.create).toHaveBeenCalledWith(leaveStatus);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leaveStatus = { id: 123 };
        spyOn(leaveStatusService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leaveStatus });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(leaveStatusService.update).toHaveBeenCalledWith(leaveStatus);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
