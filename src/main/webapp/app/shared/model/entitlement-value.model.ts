export interface IEntitlementValue {
  id?: number;
  entitlementValue?: number;
  entitlementId?: number;
  staffName?: string;
  staffId?: number;
}

export class EntitlementValue implements IEntitlementValue {
  constructor(
    public id?: number,
    public entitlementValue?: number,
    public entitlementId?: number,
    public staffName?: string,
    public staffId?: number
  ) {}
}
