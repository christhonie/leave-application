package za.co.dearx.leave.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import za.co.dearx.leave.domain.EntitlementValue;

/**
 * Spring Data SQL repository for the EntitlementValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntitlementValueRepository extends JpaRepository<EntitlementValue, Long>, JpaSpecificationExecutor<EntitlementValue> {}
