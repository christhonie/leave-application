import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LeaveApplicationTestModule } from '../../../test.module';
import { LeaveStatusUpdateComponent } from 'app/entities/leave-status/leave-status-update.component';
import { LeaveStatusService } from 'app/entities/leave-status/leave-status.service';
import { LeaveStatus } from 'app/shared/model/leave-status.model';

describe('Component Tests', () => {
  describe('LeaveStatus Management Update Component', () => {
    let comp: LeaveStatusUpdateComponent;
    let fixture: ComponentFixture<LeaveStatusUpdateComponent>;
    let service: LeaveStatusService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeaveApplicationTestModule],
        declarations: [LeaveStatusUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LeaveStatusUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeaveStatusUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LeaveStatusService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LeaveStatus(123);
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
        const entity = new LeaveStatus();
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
