export interface ILeaveType {
  id?: number;
  name?: string;
  description?: string | null;
  processName?: string | null;
  dashboardOrder?: number | null;
}

export class LeaveType implements ILeaveType {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public processName?: string | null,
    public dashboardOrder?: number | null
  ) {}
}

export function getLeaveTypeIdentifier(leaveType: ILeaveType): number | undefined {
  return leaveType.id;
}
