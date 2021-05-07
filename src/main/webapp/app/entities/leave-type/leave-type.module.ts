import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LeaveTypeComponent } from './list/leave-type.component';
import { LeaveTypeDetailComponent } from './detail/leave-type-detail.component';
import { LeaveTypeUpdateComponent } from './update/leave-type-update.component';
import { LeaveTypeDeleteDialogComponent } from './delete/leave-type-delete-dialog.component';
import { LeaveTypeRoutingModule } from './route/leave-type-routing.module';

@NgModule({
  imports: [SharedModule, LeaveTypeRoutingModule],
  declarations: [LeaveTypeComponent, LeaveTypeDetailComponent, LeaveTypeUpdateComponent, LeaveTypeDeleteDialogComponent],
  entryComponents: [LeaveTypeDeleteDialogComponent],
})
export class LeaveTypeModule {}
