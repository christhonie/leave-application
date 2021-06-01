import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDecision, getDecisionIdentifier } from '../decision.model';

export type EntityResponseType = HttpResponse<IDecision>;
export type EntityArrayResponseType = HttpResponse<IDecision[]>;

@Injectable({ providedIn: 'root' })
export class DecisionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/decisions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(decision: IDecision): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decision);
    return this.http
      .post<IDecision>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(decision: IDecision): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decision);
    return this.http
      .put<IDecision>(`${this.resourceUrl}/${getDecisionIdentifier(decision) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(decision: IDecision): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decision);
    return this.http
      .patch<IDecision>(`${this.resourceUrl}/${getDecisionIdentifier(decision) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDecision>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDecision[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDecisionToCollectionIfMissing(decisionCollection: IDecision[], ...decisionsToCheck: (IDecision | null | undefined)[]): IDecision[] {
    const decisions: IDecision[] = decisionsToCheck.filter(isPresent);
    if (decisions.length > 0) {
      const decisionCollectionIdentifiers = decisionCollection.map(decisionItem => getDecisionIdentifier(decisionItem)!);
      const decisionsToAdd = decisions.filter(decisionItem => {
        const decisionIdentifier = getDecisionIdentifier(decisionItem);
        if (decisionIdentifier == null || decisionCollectionIdentifiers.includes(decisionIdentifier)) {
          return false;
        }
        decisionCollectionIdentifiers.push(decisionIdentifier);
        return true;
      });
      return [...decisionsToAdd, ...decisionCollection];
    }
    return decisionCollection;
  }

  protected convertDateFromClient(decision: IDecision): IDecision {
    return Object.assign({}, decision, {
      decidedOn: decision.decidedOn?.isValid() ? decision.decidedOn.toJSON() : undefined,
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
      res.body.forEach((decision: IDecision) => {
        decision.decidedOn = decision.decidedOn ? dayjs(decision.decidedOn) : undefined;
      });
    }
    return res;
  }
}
