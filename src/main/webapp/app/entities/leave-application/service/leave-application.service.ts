import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILeaveApplication, getLeaveApplicationIdentifier } from '../leave-application.model';

export type EntityResponseType = HttpResponse<ILeaveApplication>;
export type EntityArrayResponseType = HttpResponse<ILeaveApplication[]>;

@Injectable({ providedIn: 'root' })
export class LeaveApplicationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/leave-applications');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(leaveApplication: ILeaveApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http
      .post<ILeaveApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaveApplication: ILeaveApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http
      .put<ILeaveApplication>(`${this.resourceUrl}/${getLeaveApplicationIdentifier(leaveApplication) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(leaveApplication: ILeaveApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http
      .patch<ILeaveApplication>(`${this.resourceUrl}/${getLeaveApplicationIdentifier(leaveApplication) as number}`, copy, {
        observe: 'response',
      })
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

  resubmit(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaveApplication>(`${this.resourceUrl}/resubmit/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  addLeaveApplicationToCollectionIfMissing(
    leaveApplicationCollection: ILeaveApplication[],
    ...leaveApplicationsToCheck: (ILeaveApplication | null | undefined)[]
  ): ILeaveApplication[] {
    const leaveApplications: ILeaveApplication[] = leaveApplicationsToCheck.filter(isPresent);
    if (leaveApplications.length > 0) {
      const leaveApplicationCollectionIdentifiers = leaveApplicationCollection.map(
        leaveApplicationItem => getLeaveApplicationIdentifier(leaveApplicationItem)!
      );
      const leaveApplicationsToAdd = leaveApplications.filter(leaveApplicationItem => {
        const leaveApplicationIdentifier = getLeaveApplicationIdentifier(leaveApplicationItem);
        if (leaveApplicationIdentifier == null || leaveApplicationCollectionIdentifiers.includes(leaveApplicationIdentifier)) {
          return false;
        }
        leaveApplicationCollectionIdentifiers.push(leaveApplicationIdentifier);
        return true;
      });
      return [...leaveApplicationsToAdd, ...leaveApplicationCollection];
    }
    return leaveApplicationCollection;
  }

  protected convertDateFromClient(leaveApplication: ILeaveApplication): ILeaveApplication {
    return Object.assign({}, leaveApplication, {
      startDate: leaveApplication.startDate?.isValid() ? leaveApplication.startDate.format(DATE_FORMAT) : undefined,
      endDate: leaveApplication.endDate?.isValid() ? leaveApplication.endDate.format(DATE_FORMAT) : undefined,
      appliedDate: leaveApplication.appliedDate?.isValid() ? leaveApplication.appliedDate.toJSON() : undefined,
      updateDate: leaveApplication.updateDate?.isValid() ? leaveApplication.updateDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
      res.body.appliedDate = res.body.appliedDate ? dayjs(res.body.appliedDate) : undefined;
      res.body.updateDate = res.body.updateDate ? dayjs(res.body.updateDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((leaveApplication: ILeaveApplication) => {
        leaveApplication.startDate = leaveApplication.startDate ? dayjs(leaveApplication.startDate) : undefined;
        leaveApplication.endDate = leaveApplication.endDate ? dayjs(leaveApplication.endDate) : undefined;
        leaveApplication.appliedDate = leaveApplication.appliedDate ? dayjs(leaveApplication.appliedDate) : undefined;
        leaveApplication.updateDate = leaveApplication.updateDate ? dayjs(leaveApplication.updateDate) : undefined;
      });
    }
    return res;
  }
}
