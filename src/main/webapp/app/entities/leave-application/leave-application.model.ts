import * as dayjs from 'dayjs';
import { ILeaveType } from 'app/entities/leave-type/leave-type.model';
import { ILeaveStatus } from 'app/entities/leave-status/leave-status.model';
import { IStaff } from 'app/entities/staff/staff.model';
import { ILeaveDeduction } from 'app/entities/leave-deduction/leave-deduction.model';

export interface ILeaveApplication {
  id?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  appliedDate?: dayjs.Dayjs;
  updateDate?: dayjs.Dayjs | null;
  days?: number;
  deleted?: boolean;
  leaveType?: ILeaveType;
  leaveStatus?: ILeaveStatus | null;
  staff?: IStaff;
  deductions?: ILeaveDeduction[] | null;
}

export class LeaveApplication implements ILeaveApplication {
  constructor(
    public id?: number,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public appliedDate?: dayjs.Dayjs,
    public updateDate?: dayjs.Dayjs | null,
    public days?: number,
    public deleted?: boolean,
    public leaveType?: ILeaveType,
    public leaveStatus?: ILeaveStatus | null,
    public staff?: IStaff,
    public deductions?: ILeaveDeduction[] | null
  ) {
    this.deleted = this.deleted ?? false;
  }
}

export function getLeaveApplicationIdentifier(leaveApplication: ILeaveApplication): number | undefined {
  return leaveApplication.id;
}
