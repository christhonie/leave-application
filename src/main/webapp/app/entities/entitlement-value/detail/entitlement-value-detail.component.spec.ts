import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EntitlementValueDetailComponent } from './entitlement-value-detail.component';

describe('Component Tests', () => {
  describe('EntitlementValue Management Detail Component', () => {
    let comp: EntitlementValueDetailComponent;
    let fixture: ComponentFixture<EntitlementValueDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EntitlementValueDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ entitlementValue: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EntitlementValueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EntitlementValueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load entitlementValue on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.entitlementValue).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
