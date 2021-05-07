jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeaveType, LeaveType } from '../leave-type.model';
import { LeaveTypeService } from '../service/leave-type.service';

import { LeaveTypeRoutingResolveService } from './leave-type-routing-resolve.service';

describe('Service Tests', () => {
  describe('LeaveType routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LeaveTypeRoutingResolveService;
    let service: LeaveTypeService;
    let resultLeaveType: ILeaveType | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LeaveTypeRoutingResolveService);
      service = TestBed.inject(LeaveTypeService);
      resultLeaveType = undefined;
    });

    describe('resolve', () => {
      it('should return ILeaveType returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeaveType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLeaveType).toEqual({ id: 123 });
      });

      it('should return new ILeaveType if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeaveType = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLeaveType).toEqual(new LeaveType());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeaveType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLeaveType).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
