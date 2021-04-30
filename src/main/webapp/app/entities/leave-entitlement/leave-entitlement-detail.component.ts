import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaveEntitlement } from 'app/shared/model/leave-entitlement.model';

@Component({
  selector: 'jhi-leave-entitlement-detail',
  templateUrl: './leave-entitlement-detail.component.html',
})
export class LeaveEntitlementDetailComponent implements OnInit {
  leaveEntitlement: ILeaveEntitlement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaveEntitlement }) => (this.leaveEntitlement = leaveEntitlement));
  }

  previousState(): void {
    window.history.back();
  }
}
