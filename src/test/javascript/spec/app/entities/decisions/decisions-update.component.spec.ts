import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LeaveApplicationTestModule } from '../../../test.module';
import { DecisionsUpdateComponent } from 'app/entities/decisions/decisions-update.component';
import { DecisionsService } from 'app/entities/decisions/decisions.service';
import { Decisions } from 'app/shared/model/decisions.model';

describe('Component Tests', () => {
  describe('Decisions Management Update Component', () => {
    let comp: DecisionsUpdateComponent;
    let fixture: ComponentFixture<DecisionsUpdateComponent>;
    let service: DecisionsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeaveApplicationTestModule],
        declarations: [DecisionsUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(DecisionsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DecisionsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DecisionsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Decisions(123);
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
        const entity = new Decisions();
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
