import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IStaff, Staff } from '../staff.model';

import { StaffService } from './staff.service';

describe('Service Tests', () => {
  describe('Staff Service', () => {
    let service: StaffService;
    let httpMock: HttpTestingController;
    let elemDefault: IStaff;
    let expectedResult: IStaff | IStaff[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(StaffService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        position: 'AAAAAAA',
        employeeID: 'AAAAAAA',
        startDate: currentDate,
        name: 'AAAAAAA',
        firstName: 'AAAAAAA',
        lastName: 'AAAAAAA',
        email: 'AAAAAAA',
        contractNumber: 'AAAAAAA',
        gender: 'AAAAAAA',
        annualLeaveEntitlement: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Staff', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Staff()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Staff', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            position: 'BBBBBB',
            employeeID: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            name: 'BBBBBB',
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            email: 'BBBBBB',
            contractNumber: 'BBBBBB',
            gender: 'BBBBBB',
            annualLeaveEntitlement: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Staff', () => {
        const patchObject = Object.assign(
          {
            position: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
          },
          new Staff()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Staff', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            position: 'BBBBBB',
            employeeID: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            name: 'BBBBBB',
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            email: 'BBBBBB',
            contractNumber: 'BBBBBB',
            gender: 'BBBBBB',
            annualLeaveEntitlement: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Staff', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addStaffToCollectionIfMissing', () => {
        it('should add a Staff to an empty array', () => {
          const staff: IStaff = { id: 123 };
          expectedResult = service.addStaffToCollectionIfMissing([], staff);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(staff);
        });

        it('should not add a Staff to an array that contains it', () => {
          const staff: IStaff = { id: 123 };
          const staffCollection: IStaff[] = [
            {
              ...staff,
            },
            { id: 456 },
          ];
          expectedResult = service.addStaffToCollectionIfMissing(staffCollection, staff);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Staff to an array that doesn't contain it", () => {
          const staff: IStaff = { id: 123 };
          const staffCollection: IStaff[] = [{ id: 456 }];
          expectedResult = service.addStaffToCollectionIfMissing(staffCollection, staff);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(staff);
        });

        it('should add only unique Staff to an array', () => {
          const staffArray: IStaff[] = [{ id: 123 }, { id: 456 }, { id: 48141 }];
          const staffCollection: IStaff[] = [{ id: 123 }];
          expectedResult = service.addStaffToCollectionIfMissing(staffCollection, ...staffArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const staff: IStaff = { id: 123 };
          const staff2: IStaff = { id: 456 };
          expectedResult = service.addStaffToCollectionIfMissing([], staff, staff2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(staff);
          expect(expectedResult).toContain(staff2);
        });

        it('should accept null and undefined values', () => {
          const staff: IStaff = { id: 123 };
          expectedResult = service.addStaffToCollectionIfMissing([], null, staff, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(staff);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
