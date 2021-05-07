import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IEntitlementValue } from 'app/shared/model/entitlement-value.model';

type EntityResponseType = HttpResponse<IEntitlementValue>;
type EntityArrayResponseType = HttpResponse<IEntitlementValue[]>;

@Injectable({ providedIn: 'root' })
export class EntitlementValueService {
  public resourceUrl = SERVER_API_URL + 'api/entitlement-values';

  constructor(protected http: HttpClient) {}

  create(entitlementValue: IEntitlementValue): Observable<EntityResponseType> {
    return this.http.post<IEntitlementValue>(this.resourceUrl, entitlementValue, { observe: 'response' });
  }

  update(entitlementValue: IEntitlementValue): Observable<EntityResponseType> {
    return this.http.put<IEntitlementValue>(this.resourceUrl, entitlementValue, { observe: 'response' });
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
}
