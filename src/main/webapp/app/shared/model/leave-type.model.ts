export interface ILeaveType {
  id?: number;
  name?: string;
  description?: string;
}

export class LeaveType implements ILeaveType {
  constructor(public id?: number, public name?: string, public description?: string) {}
}
