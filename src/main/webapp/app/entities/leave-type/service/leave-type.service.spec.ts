import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILeaveType, LeaveType } from '../leave-type.model';

import { LeaveTypeService } from './leave-type.service';

describe('Service Tests', () => {
  describe('LeaveType Service', () => {
    let service: LeaveTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: ILeaveType;
    let expectedResult: ILeaveType | ILeaveType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LeaveTypeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        processName: 'AAAAAAA',
        dashboardOrder: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a LeaveType', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LeaveType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LeaveType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            processName: 'BBBBBB',
            dashboardOrder: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LeaveType', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            processName: 'BBBBBB',
          },
          new LeaveType()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LeaveType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            processName: 'BBBBBB',
            dashboardOrder: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a LeaveType', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLeaveTypeToCollectionIfMissing', () => {
        it('should add a LeaveType to an empty array', () => {
          const leaveType: ILeaveType = { id: 123 };
          expectedResult = service.addLeaveTypeToCollectionIfMissing([], leaveType);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(leaveType);
        });

        it('should not add a LeaveType to an array that contains it', () => {
          const leaveType: ILeaveType = { id: 123 };
          const leaveTypeCollection: ILeaveType[] = [
            {
              ...leaveType,
            },
            { id: 456 },
          ];
          expectedResult = service.addLeaveTypeToCollectionIfMissing(leaveTypeCollection, leaveType);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LeaveType to an array that doesn't contain it", () => {
          const leaveType: ILeaveType = { id: 123 };
          const leaveTypeCollection: ILeaveType[] = [{ id: 456 }];
          expectedResult = service.addLeaveTypeToCollectionIfMissing(leaveTypeCollection, leaveType);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(leaveType);
        });

        it('should add only unique LeaveType to an array', () => {
          const leaveTypeArray: ILeaveType[] = [{ id: 123 }, { id: 456 }, { id: 12028 }];
          const leaveTypeCollection: ILeaveType[] = [{ id: 123 }];
          expectedResult = service.addLeaveTypeToCollectionIfMissing(leaveTypeCollection, ...leaveTypeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const leaveType: ILeaveType = { id: 123 };
          const leaveType2: ILeaveType = { id: 456 };
          expectedResult = service.addLeaveTypeToCollectionIfMissing([], leaveType, leaveType2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(leaveType);
          expect(expectedResult).toContain(leaveType2);
        });

        it('should accept null and undefined values', () => {
          const leaveType: ILeaveType = { id: 123 };
          expectedResult = service.addLeaveTypeToCollectionIfMissing([], null, leaveType, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(leaveType);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
