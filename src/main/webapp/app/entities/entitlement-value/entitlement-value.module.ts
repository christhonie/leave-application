import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { EntitlementValueComponent } from './list/entitlement-value.component';
import { EntitlementValueDetailComponent } from './detail/entitlement-value-detail.component';
import { EntitlementValueUpdateComponent } from './update/entitlement-value-update.component';
import { EntitlementValueDeleteDialogComponent } from './delete/entitlement-value-delete-dialog.component';
import { EntitlementValueRoutingModule } from './route/entitlement-value-routing.module';

@NgModule({
  imports: [SharedModule, EntitlementValueRoutingModule],
  declarations: [
    EntitlementValueComponent,
    EntitlementValueDetailComponent,
    EntitlementValueUpdateComponent,
    EntitlementValueDeleteDialogComponent,
  ],
  entryComponents: [EntitlementValueDeleteDialogComponent],
})
export class EntitlementValueModule {}
