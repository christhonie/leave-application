import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaveEntitlement } from '../leave-entitlement.model';
import { LeaveEntitlementService } from '../service/leave-entitlement.service';

@Component({
  templateUrl: './leave-entitlement-delete-dialog.component.html',
})
export class LeaveEntitlementDeleteDialogComponent {
  leaveEntitlement?: ILeaveEntitlement;

  constructor(protected leaveEntitlementService: LeaveEntitlementService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaveEntitlementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
