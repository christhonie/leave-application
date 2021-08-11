import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPublicHoliday, PublicHoliday } from '../public-holiday.model';
import { PublicHolidayService } from '../service/public-holiday.service';

@Injectable({ providedIn: 'root' })
export class PublicHolidayRoutingResolveService implements Resolve<IPublicHoliday> {
  constructor(protected service: PublicHolidayService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPublicHoliday> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((publicHoliday: HttpResponse<PublicHoliday>) => {
          if (publicHoliday.body) {
            return of(publicHoliday.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PublicHoliday());
  }
}
