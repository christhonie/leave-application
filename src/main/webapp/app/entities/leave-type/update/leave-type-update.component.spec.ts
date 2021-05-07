jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LeaveTypeService } from '../service/leave-type.service';
import { ILeaveType, LeaveType } from '../leave-type.model';

import { LeaveTypeUpdateComponent } from './leave-type-update.component';

describe('Component Tests', () => {
  describe('LeaveType Management Update Component', () => {
    let comp: LeaveTypeUpdateComponent;
    let fixture: ComponentFixture<LeaveTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let leaveTypeService: LeaveTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LeaveTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LeaveTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeaveTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      leaveTypeService = TestBed.inject(LeaveTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const leaveType: ILeaveType = { id: 456 };

        activatedRoute.data = of({ leaveType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(leaveType));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leaveType = { id: 123 };
        spyOn(leaveTypeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leaveType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: leaveType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(leaveTypeService.update).toHaveBeenCalledWith(leaveType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leaveType = new LeaveType();
        spyOn(leaveTypeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leaveType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: leaveType }));
        saveSubject.complete();

        // THEN
        expect(leaveTypeService.create).toHaveBeenCalledWith(leaveType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leaveType = { id: 123 };
        spyOn(leaveTypeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leaveType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(leaveTypeService.update).toHaveBeenCalledWith(leaveType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
