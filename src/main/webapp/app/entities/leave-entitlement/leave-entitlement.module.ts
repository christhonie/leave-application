import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LeaveEntitlementComponent } from './list/leave-entitlement.component';
import { LeaveEntitlementDetailComponent } from './detail/leave-entitlement-detail.component';
import { LeaveEntitlementUpdateComponent } from './update/leave-entitlement-update.component';
import { LeaveEntitlementDeleteDialogComponent } from './delete/leave-entitlement-delete-dialog.component';
import { LeaveEntitlementRoutingModule } from './route/leave-entitlement-routing.module';

@NgModule({
  imports: [SharedModule, LeaveEntitlementRoutingModule],
  declarations: [
    LeaveEntitlementComponent,
    LeaveEntitlementDetailComponent,
    LeaveEntitlementUpdateComponent,
    LeaveEntitlementDeleteDialogComponent,
  ],
  entryComponents: [LeaveEntitlementDeleteDialogComponent],
})
export class LeaveEntitlementModule {}
