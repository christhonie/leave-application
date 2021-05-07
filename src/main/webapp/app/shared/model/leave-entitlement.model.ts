import { Moment } from 'moment';

export interface ILeaveEntitlement {
  id?: number;
  entitlementDate?: Moment;
  days?: number;
  leaveTypeName?: string;
  leaveTypeId?: number;
  staffName?: string;
  staffId?: number;
}

export class LeaveEntitlement implements ILeaveEntitlement {
  constructor(
    public id?: number,
    public entitlementDate?: Moment,
    public days?: number,
    public leaveTypeName?: string,
    public leaveTypeId?: number,
    public staffName?: string,
    public staffId?: number
  ) {}
}
