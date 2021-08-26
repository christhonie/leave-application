package za.co.dearx.leave.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import za.co.dearx.leave.domain.LeaveEntitlement;
import za.co.dearx.leave.domain.LeaveType;
import za.co.dearx.leave.domain.Staff;
import za.co.dearx.leave.repository.LeaveEntitlementRepository;

public class AnnualLeaveEntitlementStrategy implements ILeaveEntitlementStrategy {

    private final LeaveEntitlementRepository leaveEntitlementRepository;
    private final LeaveType leaveType;

    public AnnualLeaveEntitlementStrategy(LeaveEntitlementRepository leaveEntitlementRepository) {
        this.leaveEntitlementRepository = leaveEntitlementRepository;
        this.leaveType = new LeaveType().id((long) 2);
    }

    @Override
    public void apply(Staff staff, LocalDate date) {
        leaveEntitlementRepository
            .findIfLeaveEntitlementExists(staff, date, this.leaveType)
            .orElseGet(
                () -> {
                    return leaveEntitlementRepository.save(
                        new LeaveEntitlement()
                            .staff(staff)
                            .leaveType(this.leaveType)
                            .entitlementDate(date)
                            .days(this.getMonthlyLeaveEntitlement(staff))
                    );
                }
            );
    }

    /**
     *
     * TODO We can possible remove the @Param date and use the system date
     *
     */

    @Override
    public byte getLeaveCycleNumber(Staff staff, LocalDate date) {
        return (byte) (Period.between(staff.getStartDate().withDayOfMonth(1), date).getYears() + 1);
    }

    /**
     *
     * TODO We can possible remove the @Param date and use the system date
     *
     */

    @Override
    public byte getLeaveCycleMonth(Staff staff, LocalDate date) {
        return (byte) ((date.getMonth().getValue() - staff.getStartDate().getMonth().getValue()) % 12 + 1);
    }

    public BigDecimal getMonthlyLeaveEntitlement(Staff staff) {
        return staff.getAnnualLeaveEntitlement().divide(BigDecimal.valueOf(12), 2, RoundingMode.DOWN);
    }
}
