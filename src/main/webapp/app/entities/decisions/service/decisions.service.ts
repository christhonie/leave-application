import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDecisions, getDecisionsIdentifier } from '../decisions.model';

export type EntityResponseType = HttpResponse<IDecisions>;
export type EntityArrayResponseType = HttpResponse<IDecisions[]>;

@Injectable({ providedIn: 'root' })
export class DecisionsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/decisions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(decisions: IDecisions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decisions);
    return this.http
      .post<IDecisions>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(decisions: IDecisions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decisions);
    return this.http
      .put<IDecisions>(`${this.resourceUrl}/${getDecisionsIdentifier(decisions) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(decisions: IDecisions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decisions);
    return this.http
      .patch<IDecisions>(`${this.resourceUrl}/${getDecisionsIdentifier(decisions) as number}`, copy, { observe: 'response' })
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

  addDecisionsToCollectionIfMissing(
    decisionsCollection: IDecisions[],
    ...decisionsToCheck: (IDecisions | null | undefined)[]
  ): IDecisions[] {
    const decisions: IDecisions[] = decisionsToCheck.filter(isPresent);
    if (decisions.length > 0) {
      const decisionsCollectionIdentifiers = decisionsCollection.map(decisionsItem => getDecisionsIdentifier(decisionsItem)!);
      const decisionsToAdd = decisions.filter(decisionsItem => {
        const decisionsIdentifier = getDecisionsIdentifier(decisionsItem);
        if (decisionsIdentifier == null || decisionsCollectionIdentifiers.includes(decisionsIdentifier)) {
          return false;
        }
        decisionsCollectionIdentifiers.push(decisionsIdentifier);
        return true;
      });
      return [...decisionsToAdd, ...decisionsCollection];
    }
    return decisionsCollection;
  }

  protected convertDateFromClient(decisions: IDecisions): IDecisions {
    return Object.assign({}, decisions, {
      decidedOn: decisions.decidedOn?.isValid() ? decisions.decidedOn.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.decidedOn = res.body.decidedOn ? dayjs(res.body.decidedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((decisions: IDecisions) => {
        decisions.decidedOn = decisions.decidedOn ? dayjs(decisions.decidedOn) : undefined;
      });
    }
    return res;
  }
}
