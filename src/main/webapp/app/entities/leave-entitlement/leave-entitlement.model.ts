import * as dayjs from 'dayjs';
import { ILeaveType } from 'app/entities/leave-type/leave-type.model';
import { IStaff } from 'app/entities/staff/staff.model';
import { ILeaveDeduction } from 'app/entities/leave-deduction/leave-deduction.model';

export interface ILeaveEntitlement {
  id?: number;
  entitlementDate?: dayjs.Dayjs;
  expiryDate?: dayjs.Dayjs;
  days?: number;
  leaveType?: ILeaveType;
  staff?: IStaff;
  deductions?: ILeaveDeduction[] | null;
}

export class LeaveEntitlement implements ILeaveEntitlement {
  constructor(
    public id?: number,
    public entitlementDate?: dayjs.Dayjs,
    public expiryDate?: dayjs.Dayjs,
    public days?: number,
    public leaveType?: ILeaveType,
    public staff?: IStaff,
    public deductions?: ILeaveDeduction[] | null
  ) {}
}

export function getLeaveEntitlementIdentifier(leaveEntitlement: ILeaveEntitlement): number | undefined {
  return leaveEntitlement.id;
}
