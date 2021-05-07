import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ILeaveEntitlement, LeaveEntitlement } from '../leave-entitlement.model';

import { LeaveEntitlementService } from './leave-entitlement.service';

describe('Service Tests', () => {
  describe('LeaveEntitlement Service', () => {
    let service: LeaveEntitlementService;
    let httpMock: HttpTestingController;
    let elemDefault: ILeaveEntitlement;
    let expectedResult: ILeaveEntitlement | ILeaveEntitlement[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LeaveEntitlementService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        entitlementDate: currentDate,
        days: 0,
      };
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
            id: 1,
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

      it('should partial update a LeaveEntitlement', () => {
        const patchObject = Object.assign(
          {
            entitlementDate: currentDate.format(DATE_FORMAT),
            days: 1,
          },
          new LeaveEntitlement()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            entitlementDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LeaveEntitlement', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
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

      describe('addLeaveEntitlementToCollectionIfMissing', () => {
        it('should add a LeaveEntitlement to an empty array', () => {
          const leaveEntitlement: ILeaveEntitlement = { id: 123 };
          expectedResult = service.addLeaveEntitlementToCollectionIfMissing([], leaveEntitlement);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(leaveEntitlement);
        });

        it('should not add a LeaveEntitlement to an array that contains it', () => {
          const leaveEntitlement: ILeaveEntitlement = { id: 123 };
          const leaveEntitlementCollection: ILeaveEntitlement[] = [
            {
              ...leaveEntitlement,
            },
            { id: 456 },
          ];
          expectedResult = service.addLeaveEntitlementToCollectionIfMissing(leaveEntitlementCollection, leaveEntitlement);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LeaveEntitlement to an array that doesn't contain it", () => {
          const leaveEntitlement: ILeaveEntitlement = { id: 123 };
          const leaveEntitlementCollection: ILeaveEntitlement[] = [{ id: 456 }];
          expectedResult = service.addLeaveEntitlementToCollectionIfMissing(leaveEntitlementCollection, leaveEntitlement);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(leaveEntitlement);
        });

        it('should add only unique LeaveEntitlement to an array', () => {
          const leaveEntitlementArray: ILeaveEntitlement[] = [{ id: 123 }, { id: 456 }, { id: 75190 }];
          const leaveEntitlementCollection: ILeaveEntitlement[] = [{ id: 123 }];
          expectedResult = service.addLeaveEntitlementToCollectionIfMissing(leaveEntitlementCollection, ...leaveEntitlementArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const leaveEntitlement: ILeaveEntitlement = { id: 123 };
          const leaveEntitlement2: ILeaveEntitlement = { id: 456 };
          expectedResult = service.addLeaveEntitlementToCollectionIfMissing([], leaveEntitlement, leaveEntitlement2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(leaveEntitlement);
          expect(expectedResult).toContain(leaveEntitlement2);
        });

        it('should accept null and undefined values', () => {
          const leaveEntitlement: ILeaveEntitlement = { id: 123 };
          expectedResult = service.addLeaveEntitlementToCollectionIfMissing([], null, leaveEntitlement, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(leaveEntitlement);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
