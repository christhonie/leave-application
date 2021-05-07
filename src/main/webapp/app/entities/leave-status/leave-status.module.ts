import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LeaveApplicationSharedModule } from 'app/shared/shared.module';
import { LeaveStatusComponent } from './leave-status.component';
import { LeaveStatusDetailComponent } from './leave-status-detail.component';
import { LeaveStatusUpdateComponent } from './leave-status-update.component';
import { LeaveStatusDeleteDialogComponent } from './leave-status-delete-dialog.component';
import { leaveStatusRoute } from './leave-status.route';

@NgModule({
  imports: [LeaveApplicationSharedModule, RouterModule.forChild(leaveStatusRoute)],
  declarations: [LeaveStatusComponent, LeaveStatusDetailComponent, LeaveStatusUpdateComponent, LeaveStatusDeleteDialogComponent],
  entryComponents: [LeaveStatusDeleteDialogComponent],
})
export class LeaveApplicationLeaveStatusModule {}
