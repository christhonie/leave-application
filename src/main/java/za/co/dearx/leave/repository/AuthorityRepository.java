package za.co.dearx.leave.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.dearx.leave.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
