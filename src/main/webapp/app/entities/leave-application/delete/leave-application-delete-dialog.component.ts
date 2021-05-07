import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaveApplication } from '../leave-application.model';
import { LeaveApplicationService } from '../service/leave-application.service';

@Component({
  templateUrl: './leave-application-delete-dialog.component.html',
})
export class LeaveApplicationDeleteDialogComponent {
  leaveApplication?: ILeaveApplication;

  constructor(protected leaveApplicationService: LeaveApplicationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaveApplicationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
