jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeaveApplication, LeaveApplication } from '../leave-application.model';
import { LeaveApplicationService } from '../service/leave-application.service';

import { LeaveApplicationRoutingResolveService } from './leave-application-routing-resolve.service';

describe('Service Tests', () => {
  describe('LeaveApplication routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LeaveApplicationRoutingResolveService;
    let service: LeaveApplicationService;
    let resultLeaveApplication: ILeaveApplication | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LeaveApplicationRoutingResolveService);
      service = TestBed.inject(LeaveApplicationService);
      resultLeaveApplication = undefined;
    });

    describe('resolve', () => {
      it('should return ILeaveApplication returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeaveApplication = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLeaveApplication).toEqual({ id: 123 });
      });

      it('should return new ILeaveApplication if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeaveApplication = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLeaveApplication).toEqual(new LeaveApplication());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeaveApplication = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLeaveApplication).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
