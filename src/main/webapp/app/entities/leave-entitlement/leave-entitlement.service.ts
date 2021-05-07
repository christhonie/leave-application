import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILeaveEntitlement } from 'app/shared/model/leave-entitlement.model';

type EntityResponseType = HttpResponse<ILeaveEntitlement>;
type EntityArrayResponseType = HttpResponse<ILeaveEntitlement[]>;

@Injectable({ providedIn: 'root' })
export class LeaveEntitlementService {
  public resourceUrl = SERVER_API_URL + 'api/leave-entitlements';

  constructor(protected http: HttpClient) {}

  create(leaveEntitlement: ILeaveEntitlement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveEntitlement);
    return this.http
      .post<ILeaveEntitlement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaveEntitlement: ILeaveEntitlement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveEntitlement);
    return this.http
      .put<ILeaveEntitlement>(this.resourceUrl, copy, { observe: 'response' })
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

  protected convertDateFromClient(leaveEntitlement: ILeaveEntitlement): ILeaveEntitlement {
    const copy: ILeaveEntitlement = Object.assign({}, leaveEntitlement, {
      entitlementDate:
        leaveEntitlement.entitlementDate && leaveEntitlement.entitlementDate.isValid()
          ? leaveEntitlement.entitlementDate.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.entitlementDate = res.body.entitlementDate ? moment(res.body.entitlementDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((leaveEntitlement: ILeaveEntitlement) => {
        leaveEntitlement.entitlementDate = leaveEntitlement.entitlementDate ? moment(leaveEntitlement.entitlementDate) : undefined;
      });
    }
    return res;
  }
}
