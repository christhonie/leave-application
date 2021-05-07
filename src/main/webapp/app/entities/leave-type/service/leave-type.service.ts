import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILeaveType, getLeaveTypeIdentifier } from '../leave-type.model';

export type EntityResponseType = HttpResponse<ILeaveType>;
export type EntityArrayResponseType = HttpResponse<ILeaveType[]>;

@Injectable({ providedIn: 'root' })
export class LeaveTypeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/leave-types');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(leaveType: ILeaveType): Observable<EntityResponseType> {
    return this.http.post<ILeaveType>(this.resourceUrl, leaveType, { observe: 'response' });
  }

  update(leaveType: ILeaveType): Observable<EntityResponseType> {
    return this.http.put<ILeaveType>(`${this.resourceUrl}/${getLeaveTypeIdentifier(leaveType) as number}`, leaveType, {
      observe: 'response',
    });
  }

  partialUpdate(leaveType: ILeaveType): Observable<EntityResponseType> {
    return this.http.patch<ILeaveType>(`${this.resourceUrl}/${getLeaveTypeIdentifier(leaveType) as number}`, leaveType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeaveType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaveType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLeaveTypeToCollectionIfMissing(
    leaveTypeCollection: ILeaveType[],
    ...leaveTypesToCheck: (ILeaveType | null | undefined)[]
  ): ILeaveType[] {
    const leaveTypes: ILeaveType[] = leaveTypesToCheck.filter(isPresent);
    if (leaveTypes.length > 0) {
      const leaveTypeCollectionIdentifiers = leaveTypeCollection.map(leaveTypeItem => getLeaveTypeIdentifier(leaveTypeItem)!);
      const leaveTypesToAdd = leaveTypes.filter(leaveTypeItem => {
        const leaveTypeIdentifier = getLeaveTypeIdentifier(leaveTypeItem);
        if (leaveTypeIdentifier == null || leaveTypeCollectionIdentifiers.includes(leaveTypeIdentifier)) {
          return false;
        }
        leaveTypeCollectionIdentifiers.push(leaveTypeIdentifier);
        return true;
      });
      return [...leaveTypesToAdd, ...leaveTypeCollection];
    }
    return leaveTypeCollection;
  }
}
