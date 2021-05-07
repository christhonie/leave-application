import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILeaveStatus, getLeaveStatusIdentifier } from '../leave-status.model';

export type EntityResponseType = HttpResponse<ILeaveStatus>;
export type EntityArrayResponseType = HttpResponse<ILeaveStatus[]>;

@Injectable({ providedIn: 'root' })
export class LeaveStatusService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/leave-statuses');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(leaveStatus: ILeaveStatus): Observable<EntityResponseType> {
    return this.http.post<ILeaveStatus>(this.resourceUrl, leaveStatus, { observe: 'response' });
  }

  update(leaveStatus: ILeaveStatus): Observable<EntityResponseType> {
    return this.http.put<ILeaveStatus>(`${this.resourceUrl}/${getLeaveStatusIdentifier(leaveStatus) as number}`, leaveStatus, {
      observe: 'response',
    });
  }

  partialUpdate(leaveStatus: ILeaveStatus): Observable<EntityResponseType> {
    return this.http.patch<ILeaveStatus>(`${this.resourceUrl}/${getLeaveStatusIdentifier(leaveStatus) as number}`, leaveStatus, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeaveStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaveStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLeaveStatusToCollectionIfMissing(
    leaveStatusCollection: ILeaveStatus[],
    ...leaveStatusesToCheck: (ILeaveStatus | null | undefined)[]
  ): ILeaveStatus[] {
    const leaveStatuses: ILeaveStatus[] = leaveStatusesToCheck.filter(isPresent);
    if (leaveStatuses.length > 0) {
      const leaveStatusCollectionIdentifiers = leaveStatusCollection.map(leaveStatusItem => getLeaveStatusIdentifier(leaveStatusItem)!);
      const leaveStatusesToAdd = leaveStatuses.filter(leaveStatusItem => {
        const leaveStatusIdentifier = getLeaveStatusIdentifier(leaveStatusItem);
        if (leaveStatusIdentifier == null || leaveStatusCollectionIdentifiers.includes(leaveStatusIdentifier)) {
          return false;
        }
        leaveStatusCollectionIdentifiers.push(leaveStatusIdentifier);
        return true;
      });
      return [...leaveStatusesToAdd, ...leaveStatusCollection];
    }
    return leaveStatusCollection;
  }
}
