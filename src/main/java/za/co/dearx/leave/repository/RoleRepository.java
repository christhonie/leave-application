package za.co.dearx.leave.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.dearx.leave.domain.Role;

/**
 * Spring Data  repository for the Role entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    @Query(
        value = "select distinct role from Role role left join fetch role.users",
        countQuery = "select count(distinct role) from Role role"
    )
    Page<Role> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct role from Role role left join fetch role.users")
    List<Role> findAllWithEagerRelationships();

    @Query("select role from Role role left join fetch role.users where role.id =:id")
    Optional<Role> findOneWithEagerRelationships(@Param("id") Long id);
}
