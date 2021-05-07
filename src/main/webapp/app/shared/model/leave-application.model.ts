import { Moment } from 'moment';

export interface ILeaveApplication {
  id?: number;
  startDate?: Moment;
  endDate?: Moment;
  appliedDate?: Moment;
  updateDate?: Moment;
  days?: number;
  deleted?: boolean;
  leaveTypeName?: string;
  leaveTypeId?: number;
  leaveStatusName?: string;
  leaveStatusId?: number;
  staffName?: string;
  staffId?: number;
}

export class LeaveApplication implements ILeaveApplication {
  constructor(
    public id?: number,
    public startDate?: Moment,
    public endDate?: Moment,
    public appliedDate?: Moment,
    public updateDate?: Moment,
    public days?: number,
    public deleted?: boolean,
    public leaveTypeName?: string,
    public leaveTypeId?: number,
    public leaveStatusName?: string,
    public leaveStatusId?: number,
    public staffName?: string,
    public staffId?: number
  ) {
    this.deleted = this.deleted || false;
  }
}
