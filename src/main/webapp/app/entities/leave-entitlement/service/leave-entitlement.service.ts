import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILeaveEntitlement, getLeaveEntitlementIdentifier } from '../leave-entitlement.model';

export type EntityResponseType = HttpResponse<ILeaveEntitlement>;
export type EntityArrayResponseType = HttpResponse<ILeaveEntitlement[]>;

@Injectable({ providedIn: 'root' })
export class LeaveEntitlementService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/leave-entitlements');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(leaveEntitlement: ILeaveEntitlement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveEntitlement);
    return this.http
      .post<ILeaveEntitlement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaveEntitlement: ILeaveEntitlement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveEntitlement);
    return this.http
      .put<ILeaveEntitlement>(`${this.resourceUrl}/${getLeaveEntitlementIdentifier(leaveEntitlement) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(leaveEntitlement: ILeaveEntitlement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveEntitlement);
    return this.http
      .patch<ILeaveEntitlement>(`${this.resourceUrl}/${getLeaveEntitlementIdentifier(leaveEntitlement) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaveEntitlement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaveEntitlement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLeaveEntitlementToCollectionIfMissing(
    leaveEntitlementCollection: ILeaveEntitlement[],
    ...leaveEntitlementsToCheck: (ILeaveEntitlement | null | undefined)[]
  ): ILeaveEntitlement[] {
    const leaveEntitlements: ILeaveEntitlement[] = leaveEntitlementsToCheck.filter(isPresent);
    if (leaveEntitlements.length > 0) {
      const leaveEntitlementCollectionIdentifiers = leaveEntitlementCollection.map(
        leaveEntitlementItem => getLeaveEntitlementIdentifier(leaveEntitlementItem)!
      );
      const leaveEntitlementsToAdd = leaveEntitlements.filter(leaveEntitlementItem => {
        const leaveEntitlementIdentifier = getLeaveEntitlementIdentifier(leaveEntitlementItem);
        if (leaveEntitlementIdentifier == null || leaveEntitlementCollectionIdentifiers.includes(leaveEntitlementIdentifier)) {
          return false;
        }
        leaveEntitlementCollectionIdentifiers.push(leaveEntitlementIdentifier);
        return true;
      });
      return [...leaveEntitlementsToAdd, ...leaveEntitlementCollection];
    }
    return leaveEntitlementCollection;
  }

  protected convertDateFromClient(leaveEntitlement: ILeaveEntitlement): ILeaveEntitlement {
    return Object.assign({}, leaveEntitlement, {
      entitlementDate: leaveEntitlement.entitlementDate?.isValid() ? leaveEntitlement.entitlementDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.entitlementDate = res.body.entitlementDate ? dayjs(res.body.entitlementDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((leaveEntitlement: ILeaveEntitlement) => {
        leaveEntitlement.entitlementDate = leaveEntitlement.entitlementDate ? dayjs(leaveEntitlement.entitlementDate) : undefined;
      });
    }
    return res;
  }
}
