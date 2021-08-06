import { ILeaveApplication } from 'app/entities/leave-application/leave-application.model';
import { ILeaveEntitlement } from 'app/entities/leave-entitlement/leave-entitlement.model';

export interface ILeaveDeduction {
  id?: number;
  days?: number;
  application?: ILeaveApplication;
  entitlement?: ILeaveEntitlement | null;
}

export class LeaveDeduction implements ILeaveDeduction {
  constructor(
    public id?: number,
    public days?: number,
    public application?: ILeaveApplication,
    public entitlement?: ILeaveEntitlement | null
  ) {}
}

export function getLeaveDeductionIdentifier(leaveDeduction: ILeaveDeduction): number | undefined {
  return leaveDeduction.id;
}
