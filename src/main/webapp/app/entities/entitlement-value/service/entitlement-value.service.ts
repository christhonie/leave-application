import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEntitlementValue, getEntitlementValueIdentifier } from '../entitlement-value.model';

export type EntityResponseType = HttpResponse<IEntitlementValue>;
export type EntityArrayResponseType = HttpResponse<IEntitlementValue[]>;

@Injectable({ providedIn: 'root' })
export class EntitlementValueService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/entitlement-values');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(entitlementValue: IEntitlementValue): Observable<EntityResponseType> {
    return this.http.post<IEntitlementValue>(this.resourceUrl, entitlementValue, { observe: 'response' });
  }

  update(entitlementValue: IEntitlementValue): Observable<EntityResponseType> {
    return this.http.put<IEntitlementValue>(
      `${this.resourceUrl}/${getEntitlementValueIdentifier(entitlementValue) as number}`,
      entitlementValue,
      { observe: 'response' }
    );
  }

  partialUpdate(entitlementValue: IEntitlementValue): Observable<EntityResponseType> {
    return this.http.patch<IEntitlementValue>(
      `${this.resourceUrl}/${getEntitlementValueIdentifier(entitlementValue) as number}`,
      entitlementValue,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEntitlementValue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEntitlementValue[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEntitlementValueToCollectionIfMissing(
    entitlementValueCollection: IEntitlementValue[],
    ...entitlementValuesToCheck: (IEntitlementValue | null | undefined)[]
  ): IEntitlementValue[] {
    const entitlementValues: IEntitlementValue[] = entitlementValuesToCheck.filter(isPresent);
    if (entitlementValues.length > 0) {
      const entitlementValueCollectionIdentifiers = entitlementValueCollection.map(
        entitlementValueItem => getEntitlementValueIdentifier(entitlementValueItem)!
      );
      const entitlementValuesToAdd = entitlementValues.filter(entitlementValueItem => {
        const entitlementValueIdentifier = getEntitlementValueIdentifier(entitlementValueItem);
        if (entitlementValueIdentifier == null || entitlementValueCollectionIdentifiers.includes(entitlementValueIdentifier)) {
          return false;
        }
        entitlementValueCollectionIdentifiers.push(entitlementValueIdentifier);
        return true;
      });
      return [...entitlementValuesToAdd, ...entitlementValueCollection];
    }
    return entitlementValueCollection;
  }
}
