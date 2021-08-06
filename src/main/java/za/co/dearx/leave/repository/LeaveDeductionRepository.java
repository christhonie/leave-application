package za.co.dearx.leave.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import za.co.dearx.leave.domain.LeaveDeduction;

/**
 * Spring Data SQL repository for the LeaveDeduction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveDeductionRepository extends JpaRepository<LeaveDeduction, Long>, JpaSpecificationExecutor<LeaveDeduction> {}
