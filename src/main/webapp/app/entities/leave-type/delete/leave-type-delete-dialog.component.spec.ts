jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { LeaveTypeService } from '../service/leave-type.service';

import { LeaveTypeDeleteDialogComponent } from './leave-type-delete-dialog.component';

describe('Component Tests', () => {
  describe('LeaveType Management Delete Component', () => {
    let comp: LeaveTypeDeleteDialogComponent;
    let fixture: ComponentFixture<LeaveTypeDeleteDialogComponent>;
    let service: LeaveTypeService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LeaveTypeDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(LeaveTypeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LeaveTypeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LeaveTypeService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
