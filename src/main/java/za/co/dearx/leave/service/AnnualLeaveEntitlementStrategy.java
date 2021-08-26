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

    /**
     * Apply Annual Leave Entitlement to a staff memeber on a specific day
     *
     * @param staff The {@link Staff} member the leave deductions must be applied for
     * @param fromDate The date which the leave deductions must be applied for
     *
     */
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
     * The Leave Cycle number of a staff member. This will calculate which leave cycle the staff member is in, based on a date
     *
     * @param staff The {@link Staff} member which the leave cycle is needed
     * @param date The date in which the cycle must be calculated for
     *
     * @return the Leave Cycle number
     */
    @Override
    public byte getLeaveCycleNumber(Staff staff, LocalDate date) {
        return (byte) (Period.between(staff.getStartDate().withDayOfMonth(1), date).getYears() + 1);
    }

    /**
     * The month of a staff members leave cycle. This will calculate which  month in the leave cycle the staff member is in, based on a date
     *
     * @param staff The {@link Staff} member which the leave cycle month is needed
     * @param date The date in which the month must be calculated for
     *
     * @return the Leave Cycle month
     */
    @Override
    public byte getLeaveCycleMonth(Staff staff, LocalDate date) {
        return (byte) ((date.getMonth().getValue() - staff.getStartDate().getMonth().getValue()) % 12 + 1);
    }

    /**
     * A calculation to get the Monthly leave entitlement for a staff member
     *
     * The annual leave entitlement divided by 12 to get the monthly leave entitlement
     *
     * @param staff The {@link Staff} member which the montly leave entitlement is needed
     *
     * @return the monthly leave entitlement
     */
    public BigDecimal getMonthlyLeaveEntitlement(Staff staff) {
        return staff.getAnnualLeaveEntitlement().divide(BigDecimal.valueOf(12), 2, RoundingMode.DOWN);
    }
}
