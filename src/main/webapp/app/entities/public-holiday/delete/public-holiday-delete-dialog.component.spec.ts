jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PublicHolidayService } from '../service/public-holiday.service';

import { PublicHolidayDeleteDialogComponent } from './public-holiday-delete-dialog.component';

describe('Component Tests', () => {
  describe('PublicHoliday Management Delete Component', () => {
    let comp: PublicHolidayDeleteDialogComponent;
    let fixture: ComponentFixture<PublicHolidayDeleteDialogComponent>;
    let service: PublicHolidayService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PublicHolidayDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(PublicHolidayDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PublicHolidayDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PublicHolidayService);
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
