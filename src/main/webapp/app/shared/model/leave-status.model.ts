export interface ILeaveStatus {
  id?: number;
  name?: string;
  description?: string;
}

export class LeaveStatus implements ILeaveStatus {
  constructor(public id?: number, public name?: string, public description?: string) {}
}
