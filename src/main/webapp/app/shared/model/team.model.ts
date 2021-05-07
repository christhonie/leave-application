import { IStaff } from 'app/shared/model/staff.model';

export interface ITeam {
  id?: number;
  name?: string;
  managerLogin?: string;
  managerId?: number;
  members?: IStaff[];
}

export class Team implements ITeam {
  constructor(
    public id?: number,
    public name?: string,
    public managerLogin?: string,
    public managerId?: number,
    public members?: IStaff[]
  ) {}
}
