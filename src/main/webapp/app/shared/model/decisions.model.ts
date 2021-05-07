import { Moment } from 'moment';
import { DecisionChoice } from 'app/shared/model/enumerations/decision-choice.model';

export interface IDecisions {
  id?: number;
  choice?: DecisionChoice;
  decidedOn?: Moment;
  commentComment?: string;
  commentId?: number;
  userLogin?: string;
  userId?: number;
  leaveApplicationId?: number;
}

export class Decisions implements IDecisions {
  constructor(
    public id?: number,
    public choice?: DecisionChoice,
    public decidedOn?: Moment,
    public commentComment?: string,
    public commentId?: number,
    public userLogin?: string,
    public userId?: number,
    public leaveApplicationId?: number
  ) {}
}
