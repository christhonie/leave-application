import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'role',
        data: { pageTitle: 'leaveApplicationApp.role.home.title' },
        loadChildren: () => import('./role/role.module').then(m => m.RoleModule),
      },
      {
        path: 'team',
        data: { pageTitle: 'leaveApplicationApp.team.home.title' },
        loadChildren: () => import('./team/team.module').then(m => m.TeamModule),
      },
      {
        path: 'staff',
        data: { pageTitle: 'leaveApplicationApp.staff.home.title' },
        loadChildren: () => import('./staff/staff.module').then(m => m.StaffModule),
      },
      {
        path: 'leave-status',
        data: { pageTitle: 'leaveApplicationApp.leaveStatus.home.title' },
        loadChildren: () => import('./leave-status/leave-status.module').then(m => m.LeaveStatusModule),
      },
      {
        path: 'leave-type',
        data: { pageTitle: 'leaveApplicationApp.leaveType.home.title' },
        loadChildren: () => import('./leave-type/leave-type.module').then(m => m.LeaveTypeModule),
      },
      {
        path: 'leave-entitlement',
        data: { pageTitle: 'leaveApplicationApp.leaveEntitlement.home.title' },
        loadChildren: () => import('./leave-entitlement/leave-entitlement.module').then(m => m.LeaveEntitlementModule),
      },
      {
        path: 'leave-application',
        data: { pageTitle: 'leaveApplicationApp.leaveApplication.home.title' },
        loadChildren: () => import('./leave-application/leave-application.module').then(m => m.LeaveApplicationModule),
      },
      {
        path: 'comment',
        data: { pageTitle: 'leaveApplicationApp.comment.home.title' },
        loadChildren: () => import('./comment/comment.module').then(m => m.CommentModule),
      },
      {
        path: 'decision',
        data: { pageTitle: 'leaveApplicationApp.decision.home.title' },
        loadChildren: () => import('./decision/decision.module').then(m => m.DecisionModule),
      },
      {
        path: 'public-holiday',
        data: { pageTitle: 'leaveApplicationApp.publicHoliday.home.title' },
        loadChildren: () => import('./public-holiday/public-holiday.module').then(m => m.PublicHolidayModule),
      },
      {
        path: 'leave-deduction',
        data: { pageTitle: 'leaveApplicationApp.leaveDeduction.home.title' },
        loadChildren: () => import('./leave-deduction/leave-deduction.module').then(m => m.LeaveDeductionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
