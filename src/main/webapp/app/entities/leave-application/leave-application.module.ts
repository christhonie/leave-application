import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LeaveApplicationSharedModule } from 'app/shared/shared.module';
import { LeaveApplicationComponent } from './leave-application.component';
import { LeaveApplicationDetailComponent } from './leave-application-detail.component';
import { LeaveApplicationUpdateComponent } from './leave-application-update.component';
import { LeaveApplicationDeleteDialogComponent } from './leave-application-delete-dialog.component';
import { leaveApplicationRoute } from './leave-application.route';

@NgModule({
  imports: [LeaveApplicationSharedModule, RouterModule.forChild(leaveApplicationRoute)],
  declarations: [
    LeaveApplicationComponent,
    LeaveApplicationDetailComponent,
    LeaveApplicationUpdateComponent,
    LeaveApplicationDeleteDialogComponent,
  ],
  entryComponents: [LeaveApplicationDeleteDialogComponent],
})
export class LeaveApplicationLeaveApplicationModule {}
