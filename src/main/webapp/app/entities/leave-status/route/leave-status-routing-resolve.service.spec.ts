jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeaveStatus, LeaveStatus } from '../leave-status.model';
import { LeaveStatusService } from '../service/leave-status.service';

import { LeaveStatusRoutingResolveService } from './leave-status-routing-resolve.service';

describe('Service Tests', () => {
  describe('LeaveStatus routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LeaveStatusRoutingResolveService;
    let service: LeaveStatusService;
    let resultLeaveStatus: ILeaveStatus | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LeaveStatusRoutingResolveService);
      service = TestBed.inject(LeaveStatusService);
      resultLeaveStatus = undefined;
    });

    describe('resolve', () => {
      it('should return ILeaveStatus returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeaveStatus = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLeaveStatus).toEqual({ id: 123 });
      });

      it('should return new ILeaveStatus if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeaveStatus = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLeaveStatus).toEqual(new LeaveStatus());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeaveStatus = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLeaveStatus).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
