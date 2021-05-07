import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILeaveStatus } from 'app/shared/model/leave-status.model';

type EntityResponseType = HttpResponse<ILeaveStatus>;
type EntityArrayResponseType = HttpResponse<ILeaveStatus[]>;

@Injectable({ providedIn: 'root' })
export class LeaveStatusService {
  public resourceUrl = SERVER_API_URL + 'api/leave-statuses';

  constructor(protected http: HttpClient) {}

  create(leaveStatus: ILeaveStatus): Observable<EntityResponseType> {
    return this.http.post<ILeaveStatus>(this.resourceUrl, leaveStatus, { observe: 'response' });
  }

  update(leaveStatus: ILeaveStatus): Observable<EntityResponseType> {
    return this.http.put<ILeaveStatus>(this.resourceUrl, leaveStatus, { observe: 'response' });
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
}
