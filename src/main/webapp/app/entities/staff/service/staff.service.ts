import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStaff, getStaffIdentifier } from '../staff.model';

export type EntityResponseType = HttpResponse<IStaff>;
export type EntityArrayResponseType = HttpResponse<IStaff[]>;

@Injectable({ providedIn: 'root' })
export class StaffService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/staff');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(staff: IStaff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(staff);
    return this.http
      .post<IStaff>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(staff: IStaff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(staff);
    return this.http
      .put<IStaff>(`${this.resourceUrl}/${getStaffIdentifier(staff) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(staff: IStaff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(staff);
    return this.http
      .patch<IStaff>(`${this.resourceUrl}/${getStaffIdentifier(staff) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStaff>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStaff[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStaffToCollectionIfMissing(staffCollection: IStaff[], ...staffToCheck: (IStaff | null | undefined)[]): IStaff[] {
    const staff: IStaff[] = staffToCheck.filter(isPresent);
    if (staff.length > 0) {
      const staffCollectionIdentifiers = staffCollection.map(staffItem => getStaffIdentifier(staffItem)!);
      const staffToAdd = staff.filter(staffItem => {
        const staffIdentifier = getStaffIdentifier(staffItem);
        if (staffIdentifier == null || staffCollectionIdentifiers.includes(staffIdentifier)) {
          return false;
        }
        staffCollectionIdentifiers.push(staffIdentifier);
        return true;
      });
      return [...staffToAdd, ...staffCollection];
    }
    return staffCollection;
  }

  protected convertDateFromClient(staff: IStaff): IStaff {
    return Object.assign({}, staff, {
      startDate: staff.startDate?.isValid() ? staff.startDate.format(DATE_FORMAT) : undefined,
      endDate: staff.endDate?.isValid() ? staff.endDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((staff: IStaff) => {
        staff.startDate = staff.startDate ? dayjs(staff.startDate) : undefined;
        staff.endDate = staff.endDate ? dayjs(staff.endDate) : undefined;
      });
    }
    return res;
  }
}
