jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEntitlementValue, EntitlementValue } from '../entitlement-value.model';
import { EntitlementValueService } from '../service/entitlement-value.service';

import { EntitlementValueRoutingResolveService } from './entitlement-value-routing-resolve.service';

describe('Service Tests', () => {
  describe('EntitlementValue routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EntitlementValueRoutingResolveService;
    let service: EntitlementValueService;
    let resultEntitlementValue: IEntitlementValue | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EntitlementValueRoutingResolveService);
      service = TestBed.inject(EntitlementValueService);
      resultEntitlementValue = undefined;
    });

    describe('resolve', () => {
      it('should return IEntitlementValue returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEntitlementValue = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEntitlementValue).toEqual({ id: 123 });
      });

      it('should return new IEntitlementValue if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEntitlementValue = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEntitlementValue).toEqual(new EntitlementValue());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEntitlementValue = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEntitlementValue).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
