import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { DecisionChoice } from 'app/entities/enumerations/decision-choice.model';
import { IDecisions, Decisions } from '../decisions.model';

import { DecisionsService } from './decisions.service';

describe('Service Tests', () => {
  describe('Decisions Service', () => {
    let service: DecisionsService;
    let httpMock: HttpTestingController;
    let elemDefault: IDecisions;
    let expectedResult: IDecisions | IDecisions[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DecisionsService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        choice: DecisionChoice.APPROVE,
        decidedOn: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            decidedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Decisions', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            decidedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            decidedOn: currentDate,
          },
          returnedFromService
        );

        service.create(new Decisions()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Decisions', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            choice: 'BBBBBB',
            decidedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            decidedOn: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Decisions', () => {
        const patchObject = Object.assign(
          {
            choice: 'BBBBBB',
            decidedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          new Decisions()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            decidedOn: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Decisions', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            choice: 'BBBBBB',
            decidedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            decidedOn: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Decisions', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDecisionsToCollectionIfMissing', () => {
        it('should add a Decisions to an empty array', () => {
          const decisions: IDecisions = { id: 123 };
          expectedResult = service.addDecisionsToCollectionIfMissing([], decisions);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(decisions);
        });

        it('should not add a Decisions to an array that contains it', () => {
          const decisions: IDecisions = { id: 123 };
          const decisionsCollection: IDecisions[] = [
            {
              ...decisions,
            },
            { id: 456 },
          ];
          expectedResult = service.addDecisionsToCollectionIfMissing(decisionsCollection, decisions);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Decisions to an array that doesn't contain it", () => {
          const decisions: IDecisions = { id: 123 };
          const decisionsCollection: IDecisions[] = [{ id: 456 }];
          expectedResult = service.addDecisionsToCollectionIfMissing(decisionsCollection, decisions);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(decisions);
        });

        it('should add only unique Decisions to an array', () => {
          const decisionsArray: IDecisions[] = [{ id: 123 }, { id: 456 }, { id: 27459 }];
          const decisionsCollection: IDecisions[] = [{ id: 123 }];
          expectedResult = service.addDecisionsToCollectionIfMissing(decisionsCollection, ...decisionsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const decisions: IDecisions = { id: 123 };
          const decisions2: IDecisions = { id: 456 };
          expectedResult = service.addDecisionsToCollectionIfMissing([], decisions, decisions2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(decisions);
          expect(expectedResult).toContain(decisions2);
        });

        it('should accept null and undefined values', () => {
          const decisions: IDecisions = { id: 123 };
          expectedResult = service.addDecisionsToCollectionIfMissing([], null, decisions, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(decisions);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
