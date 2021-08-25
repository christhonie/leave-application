package za.co.dearx.leave.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.dearx.leave.domain.LeaveEntitlement;
import za.co.dearx.leave.domain.LeaveType;
import za.co.dearx.leave.domain.Staff;

/**
 * Spring Data SQL repository for the LeaveEntitlement entity.
 */
@Repository
public interface LeaveEntitlementRepository extends JpaRepository<LeaveEntitlement, Long>, JpaSpecificationExecutor<LeaveEntitlement> {
    @Query("select le from LeaveEntitlement le where le.entitlementDate = :date and le.staff = :staff and le.leaveType = :leaveType")
    Optional<LeaveEntitlement> findIfLeaveEntitlementExists(
        @Param("staff") Staff staff,
        @Param("date") LocalDate date,
        @Param("leaveType") LeaveType leaveType
    );
}
