package za.co.dearx.leave.service;

import org.springframework.stereotype.Service;
import za.co.dearx.leave.domain.LeaveDeduction;
import za.co.dearx.leave.domain.LeaveEntitlement;

/**
 * This service deals with the interplay between a {@link LeaveEntitlement} and {@link LeaveDeduction}. It is used to
 * calculate the leave balance, but also the processes and data from the aforementioned entities that drive the balance calculation.
 *
 * @author Christhonie Geldenhuys
 *
 */
@Service
public class LeaveBalanceService {}
