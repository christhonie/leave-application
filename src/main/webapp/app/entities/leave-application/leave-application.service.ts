import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILeaveApplication } from 'app/shared/model/leave-application.model';

type EntityResponseType = HttpResponse<ILeaveApplication>;
type EntityArrayResponseType = HttpResponse<ILeaveApplication[]>;

@Injectable({ providedIn: 'root' })
export class LeaveApplicationService {
  public resourceUrl = SERVER_API_URL + 'api/leave-applications';

  constructor(protected http: HttpClient) {}

  create(leaveApplication: ILeaveApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http
      .post<ILeaveApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaveApplication: ILeaveApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http
      .put<ILeaveApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaveApplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaveApplication[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(leaveApplication: ILeaveApplication): ILeaveApplication {
    const copy: ILeaveApplication = Object.assign({}, leaveApplication, {
      startDate:
        leaveApplication.startDate && leaveApplication.startDate.isValid() ? leaveApplication.startDate.format(DATE_FORMAT) : undefined,
      endDate: leaveApplication.endDate && leaveApplication.endDate.isValid() ? leaveApplication.endDate.format(DATE_FORMAT) : undefined,
      appliedDate:
        leaveApplication.appliedDate && leaveApplication.appliedDate.isValid() ? leaveApplication.appliedDate.toJSON() : undefined,
      updateDate: leaveApplication.updateDate && leaveApplication.updateDate.isValid() ? leaveApplication.updateDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? moment(res.body.endDate) : undefined;
      res.body.appliedDate = res.body.appliedDate ? moment(res.body.appliedDate) : undefined;
      res.body.updateDate = res.body.updateDate ? moment(res.body.updateDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((leaveApplication: ILeaveApplication) => {
        leaveApplication.startDate = leaveApplication.startDate ? moment(leaveApplication.startDate) : undefined;
        leaveApplication.endDate = leaveApplication.endDate ? moment(leaveApplication.endDate) : undefined;
        leaveApplication.appliedDate = leaveApplication.appliedDate ? moment(leaveApplication.appliedDate) : undefined;
        leaveApplication.updateDate = leaveApplication.updateDate ? moment(leaveApplication.updateDate) : undefined;
      });
    }
    return res;
  }
}
