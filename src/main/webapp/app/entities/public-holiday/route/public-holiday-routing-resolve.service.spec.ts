jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPublicHoliday, PublicHoliday } from '../public-holiday.model';
import { PublicHolidayService } from '../service/public-holiday.service';

import { PublicHolidayRoutingResolveService } from './public-holiday-routing-resolve.service';

describe('Service Tests', () => {
  describe('PublicHoliday routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PublicHolidayRoutingResolveService;
    let service: PublicHolidayService;
    let resultPublicHoliday: IPublicHoliday | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PublicHolidayRoutingResolveService);
      service = TestBed.inject(PublicHolidayService);
      resultPublicHoliday = undefined;
    });

    describe('resolve', () => {
      it('should return IPublicHoliday returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPublicHoliday = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPublicHoliday).toEqual({ id: 123 });
      });

      it('should return new IPublicHoliday if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPublicHoliday = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPublicHoliday).toEqual(new PublicHoliday());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPublicHoliday = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPublicHoliday).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
