package za.co.dearx.leave.web.rest;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;
import za.co.dearx.leave.domain.LeaveType;
import za.co.dearx.leave.service.LeaveBalanceService;
import za.co.dearx.leave.service.dto.LeaveBalanceDTO;

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
     * {@code GET  /leave-balances/:staffId} : Get balances of the {@link Staff} member.
     *
     *
     *
     * @param staffId the ID of the staff member.
     * @param all (true) to return all or (false, default) only for {@link LeaveType}s eligible for display.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaveDeductions in body.
     */
    @GetMapping("/leave-balances/:staffId")
    public ResponseEntity<List<LeaveBalanceDTO>> getLeaveBalances(
        @PathVariable Long staffId,
        @RequestParam(defaultValue = "false") Boolean all
    ) {
        log.debug(
            "REST request to get all Leave balances for staff ID {}. Return all (even non-display) = {}",
            staffId,
            all ? "true" : "false"
        );

        final List<LeaveBalanceDTO> result = leaveBalanceService.getLeaveBalance(staffId, all);

        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET  /leave-balances/:staffId/:typeId} : Get the balance for the {@link Staff} member for a given {@link LeaveType}.
     *
     * @param id the id of the leaveDeductionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaveDeductionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leave-balances/:staffId/:typeId")
    public ResponseEntity<LeaveBalanceDTO> getLeaveBalance(@PathVariable Long staffId, @PathVariable Long typeId) {
        log.debug("REST request to get Leave balances for staff {} and type {}", staffId, typeId);

        final Optional<LeaveBalanceDTO> result = leaveBalanceService.getLeaveBalance(staffId, typeId);

        return ResponseUtil.wrapOrNotFound(result);
    }
}
