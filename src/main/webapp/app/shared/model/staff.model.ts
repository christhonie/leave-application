import { Moment } from 'moment';
import { ITeam } from 'app/shared/model/team.model';

export interface IStaff {
  id?: number;
  position?: string;
  employeeID?: string;
  startDate?: Moment;
  name?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  contractNumber?: string;
  gender?: string;
  userLogin?: string;
  userId?: number;
  teams?: ITeam[];
}

export class Staff implements IStaff {
  constructor(
    public id?: number,
    public position?: string,
    public employeeID?: string,
    public startDate?: Moment,
    public name?: string,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public contractNumber?: string,
    public gender?: string,
    public userLogin?: string,
    public userId?: number,
    public teams?: ITeam[]
  ) {}
}
