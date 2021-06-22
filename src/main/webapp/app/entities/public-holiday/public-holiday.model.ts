import * as dayjs from 'dayjs';

export interface IPublicHoliday {
  id?: number;
  name?: string | null;
  date?: dayjs.Dayjs | null;
}

export class PublicHoliday implements IPublicHoliday {
  constructor(public id?: number, public name?: string | null, public date?: dayjs.Dayjs | null) {}
}

export function getPublicHolidayIdentifier(publicHoliday: IPublicHoliday): number | undefined {
  return publicHoliday.id;
}
