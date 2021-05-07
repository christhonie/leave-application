import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { LeaveEntitlementService } from 'app/entities/leave-entitlement/leave-entitlement.service';
import { ILeaveEntitlement, LeaveEntitlement } from 'app/shared/model/leave-entitlement.model';

describe('Service Tests', () => {
  describe('LeaveEntitlement Service', () => {
    let injector: TestBed;
    let service: LeaveEntitlementService;
    let httpMock: HttpTestingController;
    let elemDefault: ILeaveEntitlement;
    let expectedResult: ILeaveEntitlement | ILeaveEntitlement[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(LeaveEntitlementService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new LeaveEntitlement(0, currentDate, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            entitlementDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a LeaveEntitlement', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            entitlementDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            entitlementDate: currentDate,
          },
          returnedFromService
        );

        service.create(new LeaveEntitlement()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LeaveEntitlement', () => {
        const returnedFromService = Object.assign(
          {
            entitlementDate: currentDate.format(DATE_FORMAT),
            days: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            entitlementDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LeaveEntitlement', () => {
        const returnedFromService = Object.assign(
          {
            entitlementDate: currentDate.format(DATE_FORMAT),
            days: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            entitlementDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a LeaveEntitlement', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
