jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PublicHolidayService } from '../service/public-holiday.service';
import { IPublicHoliday, PublicHoliday } from '../public-holiday.model';

import { PublicHolidayUpdateComponent } from './public-holiday-update.component';

describe('Component Tests', () => {
  describe('PublicHoliday Management Update Component', () => {
    let comp: PublicHolidayUpdateComponent;
    let fixture: ComponentFixture<PublicHolidayUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let publicHolidayService: PublicHolidayService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PublicHolidayUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PublicHolidayUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PublicHolidayUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      publicHolidayService = TestBed.inject(PublicHolidayService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const publicHoliday: IPublicHoliday = { id: 456 };

        activatedRoute.data = of({ publicHoliday });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(publicHoliday));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const publicHoliday = { id: 123 };
        spyOn(publicHolidayService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ publicHoliday });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: publicHoliday }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(publicHolidayService.update).toHaveBeenCalledWith(publicHoliday);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const publicHoliday = new PublicHoliday();
        spyOn(publicHolidayService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ publicHoliday });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: publicHoliday }));
        saveSubject.complete();

        // THEN
        expect(publicHolidayService.create).toHaveBeenCalledWith(publicHoliday);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const publicHoliday = { id: 123 };
        spyOn(publicHolidayService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ publicHoliday });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(publicHolidayService.update).toHaveBeenCalledWith(publicHoliday);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
