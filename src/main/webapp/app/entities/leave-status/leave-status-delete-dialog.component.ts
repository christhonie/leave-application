import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILeaveStatus } from 'app/shared/model/leave-status.model';
import { LeaveStatusService } from './leave-status.service';

@Component({
  templateUrl: './leave-status-delete-dialog.component.html',
})
export class LeaveStatusDeleteDialogComponent {
  leaveStatus?: ILeaveStatus;

  constructor(
    protected leaveStatusService: LeaveStatusService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaveStatusService.delete(id).subscribe(() => {
      this.eventManager.broadcast('leaveStatusListModification');
      this.activeModal.close();
    });
  }
}
