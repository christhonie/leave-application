package za.co.dearx.leave.repository;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.dearx.leave.domain.LeaveEntitlement;
import za.co.dearx.leave.domain.LeaveType;
import za.co.dearx.leave.domain.Staff;

/**
 * Spring Data SQL repository for the LeaveEntitlement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveEntitlementRepository extends JpaRepository<LeaveEntitlement, Long>, JpaSpecificationExecutor<LeaveEntitlement> {
    @Query(
        "select le from LeaveEntitlement le " +
        "join le.staff s " +
        "join le.leaveType lt " +
        "where s.id = :staffId and lt.id = :leaveTypeId"
    )
    LeaveEntitlement findByStaffandLeaveType(@Param("staffId") Long staffId, @Param("leaveTypeId") Long leaveTypeId);
}
