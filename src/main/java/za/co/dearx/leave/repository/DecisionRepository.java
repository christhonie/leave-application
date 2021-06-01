package za.co.dearx.leave.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import za.co.dearx.leave.domain.Decision;

/**
 * Spring Data SQL repository for the Decision entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DecisionRepository extends JpaRepository<Decision, Long>, JpaSpecificationExecutor<Decision> {
    @Query("select decision from Decision decision where decision.user.login = ?#{principal.username}")
    List<Decision> findByUserIsCurrentUser();
}
