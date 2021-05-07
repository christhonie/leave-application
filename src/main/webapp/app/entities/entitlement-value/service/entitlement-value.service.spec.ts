import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEntitlementValue, EntitlementValue } from '../entitlement-value.model';

import { EntitlementValueService } from './entitlement-value.service';

describe('Service Tests', () => {
  describe('EntitlementValue Service', () => {
    let service: EntitlementValueService;
    let httpMock: HttpTestingController;
    let elemDefault: IEntitlementValue;
    let expectedResult: IEntitlementValue | IEntitlementValue[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EntitlementValueService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        entitlementValue: 0,
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

      it('should create a EntitlementValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new EntitlementValue()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EntitlementValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            entitlementValue: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a EntitlementValue', () => {
        const patchObject = Object.assign({}, new EntitlementValue());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of EntitlementValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            entitlementValue: 1,
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

      it('should delete a EntitlementValue', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEntitlementValueToCollectionIfMissing', () => {
        it('should add a EntitlementValue to an empty array', () => {
          const entitlementValue: IEntitlementValue = { id: 123 };
          expectedResult = service.addEntitlementValueToCollectionIfMissing([], entitlementValue);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(entitlementValue);
        });

        it('should not add a EntitlementValue to an array that contains it', () => {
          const entitlementValue: IEntitlementValue = { id: 123 };
          const entitlementValueCollection: IEntitlementValue[] = [
            {
              ...entitlementValue,
            },
            { id: 456 },
          ];
          expectedResult = service.addEntitlementValueToCollectionIfMissing(entitlementValueCollection, entitlementValue);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a EntitlementValue to an array that doesn't contain it", () => {
          const entitlementValue: IEntitlementValue = { id: 123 };
          const entitlementValueCollection: IEntitlementValue[] = [{ id: 456 }];
          expectedResult = service.addEntitlementValueToCollectionIfMissing(entitlementValueCollection, entitlementValue);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(entitlementValue);
        });

        it('should add only unique EntitlementValue to an array', () => {
          const entitlementValueArray: IEntitlementValue[] = [{ id: 123 }, { id: 456 }, { id: 12600 }];
          const entitlementValueCollection: IEntitlementValue[] = [{ id: 123 }];
          expectedResult = service.addEntitlementValueToCollectionIfMissing(entitlementValueCollection, ...entitlementValueArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const entitlementValue: IEntitlementValue = { id: 123 };
          const entitlementValue2: IEntitlementValue = { id: 456 };
          expectedResult = service.addEntitlementValueToCollectionIfMissing([], entitlementValue, entitlementValue2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(entitlementValue);
          expect(expectedResult).toContain(entitlementValue2);
        });

        it('should accept null and undefined values', () => {
          const entitlementValue: IEntitlementValue = { id: 123 };
          expectedResult = service.addEntitlementValueToCollectionIfMissing([], null, entitlementValue, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(entitlementValue);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
