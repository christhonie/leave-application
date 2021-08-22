package za.co.dearx.leave.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import za.co.dearx.leave.service.LeaveBalanceService;

/**
 * REST controller for balance calculations.
 */
@RestController
@RequestMapping("/api")
public class LeaveBalanceResource {

    private final Logger log = LoggerFactory.getLogger(LeaveBalanceResource.class);

    private final LeaveBalanceService leaveBalanceService;

    public LeaveBalanceResource(LeaveBalanceService leaveBalanceService) {
        this.leaveBalanceService = leaveBalanceService;
    }
}
