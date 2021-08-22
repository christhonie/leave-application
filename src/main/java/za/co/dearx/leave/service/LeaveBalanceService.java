package za.co.dearx.leave.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import za.co.dearx.leave.domain.LeaveDeduction;
import za.co.dearx.leave.domain.LeaveEntitlement;
import za.co.dearx.leave.domain.LeaveType;
import za.co.dearx.leave.service.dto.LeaveBalanceDTO;

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
     * Get balances of the {@link Staff} member identified by staffId.
     * @param staffId of the {@link Staff} member.
     * @param all if false returns only the {@link LeaveType}s eligible for display, otherwise return all when true.
     * @return a list of {@link LeaveBalanceDTO} records.
     */
    public List<LeaveBalanceDTO> getLeaveBalance(Long staffId, Boolean all) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Get the balance for the {@link Staff} member identified by staffId for a given {@link LeaveType} identified by typeId.
     * @param staffId of the {@link Staff} member.
     * @param typeId of the {@link LeaveType}.
     * @return an optional {@link LeaveBalanceDTO} containing the balances.
     */
    public Optional<LeaveBalanceDTO> getLeaveBalance(Long staffId, Long typeId) {
        // TODO Auto-generated method stub
        return null;
    }
}
