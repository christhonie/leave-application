package za.co.dearx.leave.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.dearx.leave.domain.Staff;

/**
 * Spring Data SQL repository for the Staff entity.
 */
@Repository
public interface StaffRepository extends JpaRepository<Staff, Long>, JpaSpecificationExecutor<Staff> {
    @Query("select staff from Staff staff where staff.user.login = ?#{principal.username}")
    List<Staff> findByUserIsCurrentUser();

    @Query(
        value = "select distinct staff from Staff staff left join fetch staff.teams",
        countQuery = "select count(distinct staff) from Staff staff"
    )
    Page<Staff> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct staff from Staff staff left join fetch staff.teams")
    List<Staff> findAllWithEagerRelationships();

    @Query("select staff from Staff staff left join fetch staff.teams where staff.id =:id")
    Optional<Staff> findOneWithEagerRelationships(@Param("id") Long id);
}
