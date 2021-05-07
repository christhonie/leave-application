package za.co.dearx.leave.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import za.co.dearx.leave.domain.Team;

/**
 * Spring Data  repository for the Team entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {
    @Query("select team from Team team where team.manager.login = ?#{principal.username}")
    List<Team> findByManagerIsCurrentUser();
}
