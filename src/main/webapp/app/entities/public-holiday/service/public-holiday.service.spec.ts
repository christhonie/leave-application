import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPublicHoliday, PublicHoliday } from '../public-holiday.model';

import { PublicHolidayService } from './public-holiday.service';

describe('Service Tests', () => {
  describe('PublicHoliday Service', () => {
    let service: PublicHolidayService;
    let httpMock: HttpTestingController;
    let elemDefault: IPublicHoliday;
    let expectedResult: IPublicHoliday | IPublicHoliday[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PublicHolidayService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        date: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PublicHoliday', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new PublicHoliday()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PublicHoliday', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PublicHoliday', () => {
        const patchObject = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
          },
          new PublicHoliday()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PublicHoliday', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PublicHoliday', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPublicHolidayToCollectionIfMissing', () => {
        it('should add a PublicHoliday to an empty array', () => {
          const publicHoliday: IPublicHoliday = { id: 123 };
          expectedResult = service.addPublicHolidayToCollectionIfMissing([], publicHoliday);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(publicHoliday);
        });

        it('should not add a PublicHoliday to an array that contains it', () => {
          const publicHoliday: IPublicHoliday = { id: 123 };
          const publicHolidayCollection: IPublicHoliday[] = [
            {
              ...publicHoliday,
            },
            { id: 456 },
          ];
          expectedResult = service.addPublicHolidayToCollectionIfMissing(publicHolidayCollection, publicHoliday);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PublicHoliday to an array that doesn't contain it", () => {
          const publicHoliday: IPublicHoliday = { id: 123 };
          const publicHolidayCollection: IPublicHoliday[] = [{ id: 456 }];
          expectedResult = service.addPublicHolidayToCollectionIfMissing(publicHolidayCollection, publicHoliday);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(publicHoliday);
        });

        it('should add only unique PublicHoliday to an array', () => {
          const publicHolidayArray: IPublicHoliday[] = [{ id: 123 }, { id: 456 }, { id: 61480 }];
          const publicHolidayCollection: IPublicHoliday[] = [{ id: 123 }];
          expectedResult = service.addPublicHolidayToCollectionIfMissing(publicHolidayCollection, ...publicHolidayArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const publicHoliday: IPublicHoliday = { id: 123 };
          const publicHoliday2: IPublicHoliday = { id: 456 };
          expectedResult = service.addPublicHolidayToCollectionIfMissing([], publicHoliday, publicHoliday2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(publicHoliday);
          expect(expectedResult).toContain(publicHoliday2);
        });

        it('should accept null and undefined values', () => {
          const publicHoliday: IPublicHoliday = { id: 123 };
          expectedResult = service.addPublicHolidayToCollectionIfMissing([], null, publicHoliday, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(publicHoliday);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
