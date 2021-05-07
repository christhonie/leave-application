import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDecisions } from 'app/shared/model/decisions.model';

type EntityResponseType = HttpResponse<IDecisions>;
type EntityArrayResponseType = HttpResponse<IDecisions[]>;

@Injectable({ providedIn: 'root' })
export class DecisionsService {
  public resourceUrl = SERVER_API_URL + 'api/decisions';

  constructor(protected http: HttpClient) {}

  create(decisions: IDecisions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decisions);
    return this.http
      .post<IDecisions>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(decisions: IDecisions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decisions);
    return this.http
      .put<IDecisions>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDecisions>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDecisions[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(decisions: IDecisions): IDecisions {
    const copy: IDecisions = Object.assign({}, decisions, {
      decidedOn: decisions.decidedOn && decisions.decidedOn.isValid() ? decisions.decidedOn.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.decidedOn = res.body.decidedOn ? moment(res.body.decidedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((decisions: IDecisions) => {
        decisions.decidedOn = decisions.decidedOn ? moment(decisions.decidedOn) : undefined;
      });
    }
    return res;
  }
}
