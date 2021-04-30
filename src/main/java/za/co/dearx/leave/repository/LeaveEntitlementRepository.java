package za.co.dearx.leave.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import za.co.dearx.leave.domain.LeaveEntitlement;

/**
 * Spring Data  repository for the LeaveEntitlement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveEntitlementRepository extends JpaRepository<LeaveEntitlement, Long>, JpaSpecificationExecutor<LeaveEntitlement> {}
