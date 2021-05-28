import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DecisionComponent } from './list/decision.component';
import { DecisionDetailComponent } from './detail/decision-detail.component';
import { DecisionUpdateComponent } from './update/decision-update.component';
import { DecisionDeleteDialogComponent } from './delete/decision-delete-dialog.component';
import { DecisionRoutingModule } from './route/decision-routing.module';

@NgModule({
  imports: [SharedModule, DecisionRoutingModule],
  declarations: [DecisionComponent, DecisionDetailComponent, DecisionUpdateComponent, DecisionDeleteDialogComponent],
  entryComponents: [DecisionDeleteDialogComponent],
})
export class DecisionModule {}
