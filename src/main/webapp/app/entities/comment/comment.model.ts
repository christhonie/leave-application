import { ILeaveApplication } from 'app/entities/leave-application/leave-application.model';
import { IUser } from 'app/entities/user/user.model';

export interface IComment {
  id?: number;
  comment?: string;
  leaveApplication?: ILeaveApplication;
  user?: IUser;
}

export class Comment implements IComment {
  constructor(public id?: number, public comment?: string, public leaveApplication?: ILeaveApplication, public user?: IUser) {}
}

export function getCommentIdentifier(comment: IComment): number | undefined {
  return comment.id;
}
