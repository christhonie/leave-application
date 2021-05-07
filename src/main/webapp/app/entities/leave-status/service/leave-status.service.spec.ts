import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILeaveStatus, LeaveStatus } from '../leave-status.model';

import { LeaveStatusService } from './leave-status.service';

describe('Service Tests', () => {
  describe('LeaveStatus Service', () => {
    let service: LeaveStatusService;
    let httpMock: HttpTestingController;
    let elemDefault: ILeaveStatus;
    let expectedResult: ILeaveStatus | ILeaveStatus[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LeaveStatusService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
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

      it('should create a LeaveStatus', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LeaveStatus()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LeaveStatus', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LeaveStatus', () => {
        const patchObject = Object.assign({}, new LeaveStatus());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LeaveStatus', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
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

      it('should delete a LeaveStatus', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLeaveStatusToCollectionIfMissing', () => {
        it('should add a LeaveStatus to an empty array', () => {
          const leaveStatus: ILeaveStatus = { id: 123 };
          expectedResult = service.addLeaveStatusToCollectionIfMissing([], leaveStatus);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(leaveStatus);
        });

        it('should not add a LeaveStatus to an array that contains it', () => {
          const leaveStatus: ILeaveStatus = { id: 123 };
          const leaveStatusCollection: ILeaveStatus[] = [
            {
              ...leaveStatus,
            },
            { id: 456 },
          ];
          expectedResult = service.addLeaveStatusToCollectionIfMissing(leaveStatusCollection, leaveStatus);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LeaveStatus to an array that doesn't contain it", () => {
          const leaveStatus: ILeaveStatus = { id: 123 };
          const leaveStatusCollection: ILeaveStatus[] = [{ id: 456 }];
          expectedResult = service.addLeaveStatusToCollectionIfMissing(leaveStatusCollection, leaveStatus);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(leaveStatus);
        });

        it('should add only unique LeaveStatus to an array', () => {
          const leaveStatusArray: ILeaveStatus[] = [{ id: 123 }, { id: 456 }, { id: 89606 }];
          const leaveStatusCollection: ILeaveStatus[] = [{ id: 123 }];
          expectedResult = service.addLeaveStatusToCollectionIfMissing(leaveStatusCollection, ...leaveStatusArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const leaveStatus: ILeaveStatus = { id: 123 };
          const leaveStatus2: ILeaveStatus = { id: 456 };
          expectedResult = service.addLeaveStatusToCollectionIfMissing([], leaveStatus, leaveStatus2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(leaveStatus);
          expect(expectedResult).toContain(leaveStatus2);
        });

        it('should accept null and undefined values', () => {
          const leaveStatus: ILeaveStatus = { id: 123 };
          expectedResult = service.addLeaveStatusToCollectionIfMissing([], null, leaveStatus, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(leaveStatus);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
