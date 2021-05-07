import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LeaveStatusComponent } from './list/leave-status.component';
import { LeaveStatusDetailComponent } from './detail/leave-status-detail.component';
import { LeaveStatusUpdateComponent } from './update/leave-status-update.component';
import { LeaveStatusDeleteDialogComponent } from './delete/leave-status-delete-dialog.component';
import { LeaveStatusRoutingModule } from './route/leave-status-routing.module';

@NgModule({
  imports: [SharedModule, LeaveStatusRoutingModule],
  declarations: [LeaveStatusComponent, LeaveStatusDetailComponent, LeaveStatusUpdateComponent, LeaveStatusDeleteDialogComponent],
  entryComponents: [LeaveStatusDeleteDialogComponent],
})
export class LeaveStatusModule {}
