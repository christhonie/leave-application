import { IUser } from 'app/entities/user/user.model';
import { IStaff } from 'app/entities/staff/staff.model';

export interface ITeam {
  id?: number;
  name?: string;
  manager?: IUser | null;
  members?: IStaff[] | null;
}

export class Team implements ITeam {
  constructor(public id?: number, public name?: string, public manager?: IUser | null, public members?: IStaff[] | null) {}
}

export function getTeamIdentifier(team: ITeam): number | undefined {
  return team.id;
}
