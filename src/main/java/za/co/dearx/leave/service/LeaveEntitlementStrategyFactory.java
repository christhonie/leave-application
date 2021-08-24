package za.co.dearx.leave.service;

import java.util.Optional;
import za.co.dearx.leave.domain.LeaveEntitlement;
import za.co.dearx.leave.domain.LeaveType;

public class LeaveEntitlementStrategyFactory {

    /**
     * Returns a concrete implementation of the appropriate strategy to apply {@link LeaveEntitlement}s.
     * @param type of entitlement
     * @return an optional strategy for the {@link LeaveType} provided.
     */
    public static Optional<ILeaveEntitlementStrategy> create(LeaveType type) {
        if (type != null && type.getId() <= Byte.MAX_VALUE) {
            final int t = type.getId().byteValue();
            //The supported Entitlement strategies are listed as case statements.
            switch (t) {
                case 2:
                    return Optional.of(new AnnualLeaveEntitlementStrategy());
                //LeaveTypes without entitlement strategies get an empty response.
                default:
                    return Optional.empty();
            }
        }
        //Other LeaveTypes also get an empty response.
        return Optional.empty();
    }
}
