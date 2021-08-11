import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPublicHoliday, getPublicHolidayIdentifier } from '../public-holiday.model';

export type EntityResponseType = HttpResponse<IPublicHoliday>;
export type EntityArrayResponseType = HttpResponse<IPublicHoliday[]>;

@Injectable({ providedIn: 'root' })
export class PublicHolidayService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/public-holidays');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(publicHoliday: IPublicHoliday): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(publicHoliday);
    return this.http
      .post<IPublicHoliday>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(publicHoliday: IPublicHoliday): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(publicHoliday);
    return this.http
      .put<IPublicHoliday>(`${this.resourceUrl}/${getPublicHolidayIdentifier(publicHoliday) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(publicHoliday: IPublicHoliday): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(publicHoliday);
    return this.http
      .patch<IPublicHoliday>(`${this.resourceUrl}/${getPublicHolidayIdentifier(publicHoliday) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPublicHoliday>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPublicHoliday[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  reload(): Observable<HttpResponse<{}>> {
    return this.http.get(`${this.resourceUrl}/reload`, { observe: 'response' });
  }

  calculdateWorkDaysBetween(startDate: dayjs.Dayjs, endDate: dayjs.Dayjs): Observable<HttpResponse<number>> {
    return this.http.get<number>(
      `${this.resourceUrl}/work-days?startDate=${startDate.format('YYYY-MM-DD')}&endDate=${endDate.format('YYYY-MM-DD')}`,
      { observe: 'response' }
    );
  }

  addPublicHolidayToCollectionIfMissing(
    publicHolidayCollection: IPublicHoliday[],
    ...publicHolidaysToCheck: (IPublicHoliday | null | undefined)[]
  ): IPublicHoliday[] {
    const publicHolidays: IPublicHoliday[] = publicHolidaysToCheck.filter(isPresent);
    if (publicHolidays.length > 0) {
      const publicHolidayCollectionIdentifiers = publicHolidayCollection.map(
        publicHolidayItem => getPublicHolidayIdentifier(publicHolidayItem)!
      );
      const publicHolidaysToAdd = publicHolidays.filter(publicHolidayItem => {
        const publicHolidayIdentifier = getPublicHolidayIdentifier(publicHolidayItem);
        if (publicHolidayIdentifier == null || publicHolidayCollectionIdentifiers.includes(publicHolidayIdentifier)) {
          return false;
        }
        publicHolidayCollectionIdentifiers.push(publicHolidayIdentifier);
        return true;
      });
      return [...publicHolidaysToAdd, ...publicHolidayCollection];
    }
    return publicHolidayCollection;
  }

  protected convertDateFromClient(publicHoliday: IPublicHoliday): IPublicHoliday {
    return Object.assign({}, publicHoliday, {
      date: publicHoliday.date?.isValid() ? publicHoliday.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((publicHoliday: IPublicHoliday) => {
        publicHoliday.date = publicHoliday.date ? dayjs(publicHoliday.date) : undefined;
      });
    }
    return res;
  }
}
