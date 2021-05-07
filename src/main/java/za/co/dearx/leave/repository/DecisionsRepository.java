package za.co.dearx.leave.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import za.co.dearx.leave.domain.Decisions;

/**
 * Spring Data SQL repository for the Decisions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DecisionsRepository extends JpaRepository<Decisions, Long>, JpaSpecificationExecutor<Decisions> {
    @Query("select decisions from Decisions decisions where decisions.user.login = ?#{principal.username}")
    List<Decisions> findByUserIsCurrentUser();
}
