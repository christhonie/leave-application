jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IStaff, Staff } from '../staff.model';
import { StaffService } from '../service/staff.service';

import { StaffRoutingResolveService } from './staff-routing-resolve.service';

describe('Service Tests', () => {
  describe('Staff routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: StaffRoutingResolveService;
    let service: StaffService;
    let resultStaff: IStaff | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(StaffRoutingResolveService);
      service = TestBed.inject(StaffService);
      resultStaff = undefined;
    });

    describe('resolve', () => {
      it('should return IStaff returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStaff = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStaff).toEqual({ id: 123 });
      });

      it('should return new IStaff if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStaff = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultStaff).toEqual(new Staff());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStaff = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStaff).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
