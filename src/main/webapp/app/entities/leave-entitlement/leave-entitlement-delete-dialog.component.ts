import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILeaveEntitlement } from 'app/shared/model/leave-entitlement.model';
import { LeaveEntitlementService } from './leave-entitlement.service';

@Component({
  templateUrl: './leave-entitlement-delete-dialog.component.html',
})
export class LeaveEntitlementDeleteDialogComponent {
  leaveEntitlement?: ILeaveEntitlement;

  constructor(
    protected leaveEntitlementService: LeaveEntitlementService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaveEntitlementService.delete(id).subscribe(() => {
      this.eventManager.broadcast('leaveEntitlementListModification');
      this.activeModal.close();
    });
  }
}
