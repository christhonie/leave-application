import * as dayjs from 'dayjs';
import { IComment } from 'app/entities/comment/comment.model';
import { IUser } from 'app/entities/user/user.model';
import { ILeaveApplication } from 'app/entities/leave-application/leave-application.model';
import { DecisionChoice } from 'app/entities/enumerations/decision-choice.model';

export interface IDecisions {
  id?: number;
  choice?: DecisionChoice;
  decidedOn?: dayjs.Dayjs;
  comment?: IComment | null;
  user?: IUser;
  leaveApplication?: ILeaveApplication;
}

export class Decisions implements IDecisions {
  constructor(
    public id?: number,
    public choice?: DecisionChoice,
    public decidedOn?: dayjs.Dayjs,
    public comment?: IComment | null,
    public user?: IUser,
    public leaveApplication?: ILeaveApplication
  ) {}
}

export function getDecisionsIdentifier(decisions: IDecisions): number | undefined {
  return decisions.id;
}
