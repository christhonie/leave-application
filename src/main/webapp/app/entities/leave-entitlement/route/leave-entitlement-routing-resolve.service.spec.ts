jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeaveEntitlement, LeaveEntitlement } from '../leave-entitlement.model';
import { LeaveEntitlementService } from '../service/leave-entitlement.service';

import { LeaveEntitlementRoutingResolveService } from './leave-entitlement-routing-resolve.service';

describe('Service Tests', () => {
  describe('LeaveEntitlement routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LeaveEntitlementRoutingResolveService;
    let service: LeaveEntitlementService;
    let resultLeaveEntitlement: ILeaveEntitlement | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LeaveEntitlementRoutingResolveService);
      service = TestBed.inject(LeaveEntitlementService);
      resultLeaveEntitlement = undefined;
    });

    describe('resolve', () => {
      it('should return ILeaveEntitlement returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeaveEntitlement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLeaveEntitlement).toEqual({ id: 123 });
      });

      it('should return new ILeaveEntitlement if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeaveEntitlement = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLeaveEntitlement).toEqual(new LeaveEntitlement());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeaveEntitlement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLeaveEntitlement).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
