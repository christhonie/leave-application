package za.co.dearx.leave.service;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import za.co.dearx.leave.domain.LeaveDeduction;
import za.co.dearx.leave.domain.LeaveEntitlement;
import za.co.dearx.leave.domain.Staff;

/**
 * This service deals with the interplay between a {@link LeaveEntitlement} and {@link LeaveDeduction}. It is used to
 * calculate the leave balance, but also the processes and data from the aforementioned entities that drive the balance calculation.
 *
 * @author Christhonie Geldenhuys
 *
 */
@Service
public class LeaveBalanceService {

    /**
     * Remove all existing {@link LeaveDeduction}s and reapply them for a given {@link Staff} member on or after the given fromDate.
     * @param staffId of the {@link Staff} member.
     * @param fromDate when deductions have re-applied.
     */
    public void reapplyDeductions(Long staffId, LocalDate fromDate) {
        // TODO Auto-generated method stub

    }
}
