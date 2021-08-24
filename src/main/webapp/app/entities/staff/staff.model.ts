import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { ITeam } from 'app/entities/team/team.model';

export interface IStaff {
  id?: number;
  position?: string | null;
  employeeID?: string;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs | null;
  name?: string | null;
  firstName?: string;
  lastName?: string;
  email?: string | null;
  contractNumber?: string | null;
  gender?: string;
  annualLeaveEntitlement?: number | null;
  user?: IUser | null;
  teams?: ITeam[] | null;
}

export class Staff implements IStaff {
  constructor(
    public id?: number,
    public position?: string | null,
    public employeeID?: string,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs | null,
    public name?: string | null,
    public firstName?: string,
    public lastName?: string,
    public email?: string | null,
    public contractNumber?: string | null,
    public gender?: string,
    public annualLeaveEntitlement?: number | null,
    public user?: IUser | null,
    public teams?: ITeam[] | null
  ) {}
}

export function getStaffIdentifier(staff: IStaff): number | undefined {
  return staff.id;
}
