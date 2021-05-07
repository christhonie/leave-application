import { IUser } from 'app/entities/user/user.model';

export interface IRole {
  id?: number;
  name?: string;
  users?: IUser[] | null;
}

export class Role implements IRole {
  constructor(public id?: number, public name?: string, public users?: IUser[] | null) {}
}

export function getRoleIdentifier(role: IRole): number | undefined {
  return role.id;
}
