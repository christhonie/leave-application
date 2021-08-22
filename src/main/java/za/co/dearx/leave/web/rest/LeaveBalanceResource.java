package za.co.dearx.leave.web.rest;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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

    /**
     * {@code PUT  /leave-balances/reapply} : Re-apply the leave deductions for a given staff member from the given start date.
     *
     * During this process all leave balance calculations and allocations will be reset from the start date to the current date.
     *
     * @return status {@code 200 (OK)} once calculations are done,
     * or with status {@code 400 (Bad Request)} if the parameters is not valid,
     * or with status {@code 500 (Internal Server Error)} if the process could not be performed.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leave-deductions/reapply")
    public ResponseEntity<Void> reapply(@RequestParam Long staffId, @RequestParam LocalDate fromDate) {
        leaveBalanceService.reapplyDeductions(staffId, fromDate);
        return ResponseEntity.ok().build();
    }
}
