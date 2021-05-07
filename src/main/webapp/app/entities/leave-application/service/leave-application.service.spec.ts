import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILeaveApplication, LeaveApplication } from '../leave-application.model';

import { LeaveApplicationService } from './leave-application.service';

describe('Service Tests', () => {
  describe('LeaveApplication Service', () => {
    let service: LeaveApplicationService;
    let httpMock: HttpTestingController;
    let elemDefault: ILeaveApplication;
    let expectedResult: ILeaveApplication | ILeaveApplication[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LeaveApplicationService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        startDate: currentDate,
        endDate: currentDate,
        appliedDate: currentDate,
        updateDate: currentDate,
        days: 0,
        deleted: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            appliedDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a LeaveApplication', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            appliedDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            appliedDate: currentDate,
            updateDate: currentDate,
          },
          returnedFromService
        );

        service.create(new LeaveApplication()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LeaveApplication', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            appliedDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
            days: 1,
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            appliedDate: currentDate,
            updateDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LeaveApplication', () => {
        const patchObject = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            appliedDate: currentDate.format(DATE_TIME_FORMAT),
            days: 1,
          },
          new LeaveApplication()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            appliedDate: currentDate,
            updateDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LeaveApplication', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            appliedDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
            days: 1,
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            appliedDate: currentDate,
            updateDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a LeaveApplication', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLeaveApplicationToCollectionIfMissing', () => {
        it('should add a LeaveApplication to an empty array', () => {
          const leaveApplication: ILeaveApplication = { id: 123 };
          expectedResult = service.addLeaveApplicationToCollectionIfMissing([], leaveApplication);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(leaveApplication);
        });

        it('should not add a LeaveApplication to an array that contains it', () => {
          const leaveApplication: ILeaveApplication = { id: 123 };
          const leaveApplicationCollection: ILeaveApplication[] = [
            {
              ...leaveApplication,
            },
            { id: 456 },
          ];
          expectedResult = service.addLeaveApplicationToCollectionIfMissing(leaveApplicationCollection, leaveApplication);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LeaveApplication to an array that doesn't contain it", () => {
          const leaveApplication: ILeaveApplication = { id: 123 };
          const leaveApplicationCollection: ILeaveApplication[] = [{ id: 456 }];
          expectedResult = service.addLeaveApplicationToCollectionIfMissing(leaveApplicationCollection, leaveApplication);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(leaveApplication);
        });

        it('should add only unique LeaveApplication to an array', () => {
          const leaveApplicationArray: ILeaveApplication[] = [{ id: 123 }, { id: 456 }, { id: 46770 }];
          const leaveApplicationCollection: ILeaveApplication[] = [{ id: 123 }];
          expectedResult = service.addLeaveApplicationToCollectionIfMissing(leaveApplicationCollection, ...leaveApplicationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const leaveApplication: ILeaveApplication = { id: 123 };
          const leaveApplication2: ILeaveApplication = { id: 456 };
          expectedResult = service.addLeaveApplicationToCollectionIfMissing([], leaveApplication, leaveApplication2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(leaveApplication);
          expect(expectedResult).toContain(leaveApplication2);
        });

        it('should accept null and undefined values', () => {
          const leaveApplication: ILeaveApplication = { id: 123 };
          expectedResult = service.addLeaveApplicationToCollectionIfMissing([], null, leaveApplication, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(leaveApplication);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
