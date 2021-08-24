package za.co.dearx.leave.service;

import java.time.LocalDate;
import za.co.dearx.leave.domain.LeaveEntitlement;
import za.co.dearx.leave.domain.Staff;

public interface ILeaveEntitlementStrategy {
    /**
     * Add {@link LeaveEntitlement}s to the given {@link Staff} member on the given date using the underlying concrete implementation.
     * @param staff to which to apply the entitlement
     * @param date during the period for which {@link LeaveEntitlement}s have to be added.
     * This could be any date within range of the entitlement period, inclusive of the start date or the end date of the period.
     * For instance, a monthly entitlement which should start on 1 Jan to 31 Jan could have a valid date of 1 Jan or even 31 Jan.
     */
    void apply(Staff staff, LocalDate date);
}
