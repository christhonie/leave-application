import * as dayjs from 'dayjs';
import { ILeaveType } from 'app/entities/leave-type/leave-type.model';
import { IStaff } from 'app/entities/staff/staff.model';

export interface ILeaveEntitlement {
  id?: number;
  entitlementDate?: dayjs.Dayjs;
  days?: number;
  leaveType?: ILeaveType;
  staff?: IStaff;
}

export class LeaveEntitlement implements ILeaveEntitlement {
  constructor(
    public id?: number,
    public entitlementDate?: dayjs.Dayjs,
    public days?: number,
    public leaveType?: ILeaveType,
    public staff?: IStaff
  ) {}
}

export function getLeaveEntitlementIdentifier(leaveEntitlement: ILeaveEntitlement): number | undefined {
  return leaveEntitlement.id;
}
