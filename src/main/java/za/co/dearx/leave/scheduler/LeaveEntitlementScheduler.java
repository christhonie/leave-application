package za.co.dearx.leave.scheduler;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import za.co.dearx.leave.service.LeaveEntitlementService;

/**
 * This class contains the scheduler that runs each month to apply the LeaveEntitlements.
 *
 * @author Christhonie Geldenhuys
 *
 */
public class LeaveEntitlementScheduler {

    private final Logger log = LoggerFactory.getLogger(LeaveEntitlementScheduler.class);

    final LeaveEntitlementService service;

    public LeaveEntitlementScheduler(LeaveEntitlementService service) {
        this.service = service;
    }

    /**
     * Scheduler to update the LeaveEntitlements at 5mins past midnight on the first day of the month.
     */
    @Scheduled(cron = "0 5 0 1 * ?")
    public void update() {
        //Set to date of previous month
        final LocalDate date = LocalDate.now().minusMonths(1);
        service.apply(date);

        log.debug("Leave entitlements updated using scheduler");
    }
}
