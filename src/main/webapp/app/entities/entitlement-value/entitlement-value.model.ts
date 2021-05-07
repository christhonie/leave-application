import { ILeaveEntitlement } from 'app/entities/leave-entitlement/leave-entitlement.model';
import { IStaff } from 'app/entities/staff/staff.model';

export interface IEntitlementValue {
  id?: number;
  entitlementValue?: number;
  entitlement?: ILeaveEntitlement;
  staff?: IStaff;
}

export class EntitlementValue implements IEntitlementValue {
  constructor(public id?: number, public entitlementValue?: number, public entitlement?: ILeaveEntitlement, public staff?: IStaff) {}
}

export function getEntitlementValueIdentifier(entitlementValue: IEntitlementValue): number | undefined {
  return entitlementValue.id;
}
