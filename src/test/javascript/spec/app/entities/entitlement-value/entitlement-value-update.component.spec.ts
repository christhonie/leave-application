import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LeaveApplicationTestModule } from '../../../test.module';
import { EntitlementValueUpdateComponent } from 'app/entities/entitlement-value/entitlement-value-update.component';
import { EntitlementValueService } from 'app/entities/entitlement-value/entitlement-value.service';
import { EntitlementValue } from 'app/shared/model/entitlement-value.model';

describe('Component Tests', () => {
  describe('EntitlementValue Management Update Component', () => {
    let comp: EntitlementValueUpdateComponent;
    let fixture: ComponentFixture<EntitlementValueUpdateComponent>;
    let service: EntitlementValueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeaveApplicationTestModule],
        declarations: [EntitlementValueUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(EntitlementValueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EntitlementValueUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EntitlementValueService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new EntitlementValue(123);
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
        const entity = new EntitlementValue();
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
