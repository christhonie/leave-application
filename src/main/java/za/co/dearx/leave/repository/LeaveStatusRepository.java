package za.co.dearx.leave.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import za.co.dearx.leave.domain.LeaveStatus;

/**
 * Spring Data  repository for the LeaveStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveStatusRepository extends JpaRepository<LeaveStatus, Long>, JpaSpecificationExecutor<LeaveStatus> {}
