package za.co.dearx.leave.service;

import java.time.LocalDate;
import za.co.dearx.leave.domain.Staff;
import za.co.dearx.leave.repository.LeaveEntitlementRepository;

public class AnnualLeaveEntitlementStrategy implements ILeaveEntitlementStrategy {

    private final LeaveEntitlementRepository leaveEntitlementRepository;

    public AnnualLeaveEntitlementStrategy(LeaveEntitlementRepository leaveEntitlementRepository) {
        this.leaveEntitlementRepository = leaveEntitlementRepository;
    }

    @Override
    public void apply(Staff staff, LocalDate date) {
        // TODO Auto-generated method stub
        //getLeaveEntitleMent for Staff
        //check if leaveEnt already exists
        //create leave entitlement
    }

    @Override
    public byte getLeaveCycleNumber(Staff staff, LocalDate date) {
        return 0; //TODO: Implement Theunis
    }

    @Override
    public byte getLeaveCycleMonth(Staff staff, LocalDate date) {
        return 0; //TODO: Implement Theunis
    }
}
