import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDecisions, Decisions } from 'app/shared/model/decisions.model';
import { DecisionsService } from './decisions.service';
import { DecisionsComponent } from './decisions.component';
import { DecisionsDetailComponent } from './decisions-detail.component';
import { DecisionsUpdateComponent } from './decisions-update.component';

@Injectable({ providedIn: 'root' })
export class DecisionsResolve implements Resolve<IDecisions> {
  constructor(private service: DecisionsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDecisions> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((decisions: HttpResponse<Decisions>) => {
          if (decisions.body) {
            return of(decisions.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Decisions());
  }
}

export const decisionsRoute: Routes = [
  {
    path: '',
    component: DecisionsComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'leaveApplicationApp.decisions.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DecisionsDetailComponent,
    resolve: {
      decisions: DecisionsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'leaveApplicationApp.decisions.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DecisionsUpdateComponent,
    resolve: {
      decisions: DecisionsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'leaveApplicationApp.decisions.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DecisionsUpdateComponent,
    resolve: {
      decisions: DecisionsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'leaveApplicationApp.decisions.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
