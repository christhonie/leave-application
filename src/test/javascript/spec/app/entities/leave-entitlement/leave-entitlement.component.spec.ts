import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { LeaveApplicationTestModule } from '../../../test.module';
import { LeaveEntitlementComponent } from 'app/entities/leave-entitlement/leave-entitlement.component';
import { LeaveEntitlementService } from 'app/entities/leave-entitlement/leave-entitlement.service';
import { LeaveEntitlement } from 'app/shared/model/leave-entitlement.model';

describe('Component Tests', () => {
  describe('LeaveEntitlement Management Component', () => {
    let comp: LeaveEntitlementComponent;
    let fixture: ComponentFixture<LeaveEntitlementComponent>;
    let service: LeaveEntitlementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeaveApplicationTestModule],
        declarations: [LeaveEntitlementComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({
                defaultSort: 'id,asc',
              }),
              queryParamMap: of(
                convertToParamMap({
                  page: '1',
                  size: '1',
                  sort: 'id,desc',
                })
              ),
            },
          },
        ],
      })
        .overrideTemplate(LeaveEntitlementComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeaveEntitlementComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LeaveEntitlementService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LeaveEntitlement(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.leaveEntitlements && comp.leaveEntitlements[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LeaveEntitlement(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.leaveEntitlements && comp.leaveEntitlements[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
