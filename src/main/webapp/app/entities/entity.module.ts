import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'role',
        loadChildren: () => import('./role/role.module').then(m => m.LeaveApplicationRoleModule),
      },
      {
        path: 'team',
        loadChildren: () => import('./team/team.module').then(m => m.LeaveApplicationTeamModule),
      },
      {
        path: 'staff',
        loadChildren: () => import('./staff/staff.module').then(m => m.LeaveApplicationStaffModule),
      },
      {
        path: 'leave-status',
        loadChildren: () => import('./leave-status/leave-status.module').then(m => m.LeaveApplicationLeaveStatusModule),
      },
      {
        path: 'leave-type',
        loadChildren: () => import('./leave-type/leave-type.module').then(m => m.LeaveApplicationLeaveTypeModule),
      },
      {
        path: 'leave-entitlement',
        loadChildren: () => import('./leave-entitlement/leave-entitlement.module').then(m => m.LeaveApplicationLeaveEntitlementModule),
      },
      {
        path: 'entitlement-value',
        loadChildren: () => import('./entitlement-value/entitlement-value.module').then(m => m.LeaveApplicationEntitlementValueModule),
      },
      {
        path: 'leave-application',
        loadChildren: () => import('./leave-application/leave-application.module').then(m => m.LeaveApplicationLeaveApplicationModule),
      },
      {
        path: 'comment',
        loadChildren: () => import('./comment/comment.module').then(m => m.LeaveApplicationCommentModule),
      },
      {
        path: 'decisions',
        loadChildren: () => import('./decisions/decisions.module').then(m => m.LeaveApplicationDecisionsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class LeaveApplicationEntityModule {}
