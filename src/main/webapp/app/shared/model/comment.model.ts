export interface IComment {
  id?: number;
  comment?: string;
  leaveApplicationId?: number;
  userLogin?: string;
  userId?: number;
}

export class Comment implements IComment {
  constructor(
    public id?: number,
    public comment?: string,
    public leaveApplicationId?: number,
    public userLogin?: string,
    public userId?: number
  ) {}
}
