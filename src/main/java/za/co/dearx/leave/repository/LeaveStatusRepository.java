package za.co.dearx.leave.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import za.co.dearx.leave.domain.LeaveStatus;

/**
 * Spring Data SQL repository for the LeaveStatus entity.
 */
@Repository
public interface LeaveStatusRepository extends JpaRepository<LeaveStatus, Long>, JpaSpecificationExecutor<LeaveStatus> {
    Optional<LeaveStatus> findByName(String statusString);
}
